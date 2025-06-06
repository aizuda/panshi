/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.handler;

import com.aizuda.common.toolkit.CollectionUtils;
import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.limiter.annotation.DistributedLock;
import com.aizuda.limiter.context.DistributedContext;
import com.aizuda.limiter.distributedlock.DistributedLockCallback;
import com.aizuda.limiter.distributedlock.IDistributedLockTemplate;
import com.aizuda.limiter.exception.AcquireLockTimeoutException;
import com.aizuda.limiter.extend.IAcquireLockTimeoutHandler;
import com.aizuda.limiter.extend.IDistributedLockListener;
import com.aizuda.limiter.metadata.MethodMetadata;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.convert.DurationStyle;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁处理器 Redis 实现
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 1.1.0
 */
@Slf4j
@AllArgsConstructor
public class RedisDistributedLockHandler implements IDistributedLockHandler {
    private final RateLimitKeyParser rateLimitKeyParser;
    private final DistributedContext distributedContext;

    @Override
    public Object proceed(ProceedingJoinPoint pjp, MethodMetadata methodMetadata) {
        // 获取分布式锁超时处理器
        IAcquireLockTimeoutHandler acquireLockTimeoutHandler = distributedContext.getAcquireLockTimeoutHandler(methodMetadata);
        // 监听器
        List<IDistributedLockListener> distributedLimitListeners = distributedContext.getDistributedLimitListeners(methodMetadata);

        DistributedLock distributedLock = methodMetadata.getAnnotation();

        final String lockKey = rateLimitKeyParser.buildKey(methodMetadata, distributedLock.key(), distributedLock.strategy(),
                distributedLock.useDefaultStrategy());

        IDistributedLockTemplate distributedLockTemplate = distributedContext.getDistributedLockTemplate();
        long expire = DurationStyle.detectAndParse(distributedLock.tryAcquireTimeout()).getSeconds();

        DistributedLockCallback callback = this.builderDistributedLockCallback(pjp, methodMetadata, acquireLockTimeoutHandler,
                distributedLimitListeners, distributedLockTemplate.completeLockKey(lockKey));

        beforeDistributedLock(methodMetadata, distributedLimitListeners, distributedLockTemplate.completeLockKey(lockKey));
        try {
            return distributedLockTemplate.execute(lockKey, expire, TimeUnit.SECONDS, callback);
        } finally {
            distributedLockFinally(methodMetadata, distributedLimitListeners, distributedLockTemplate.completeLockKey(lockKey));
        }
    }

    private DistributedLockCallback builderDistributedLockCallback(ProceedingJoinPoint joinPoint, MethodMetadata methodMetadata,
                                                                   IAcquireLockTimeoutHandler acquireLockTimeoutHandler,
                                                                   List<IDistributedLockListener> distributedLimitListeners,
                                                                   String lockKey) {
        return new DistributedLockCallback() {

            @SneakyThrows
            @Override
            public Object onGetLock() {
                if (log.isDebugEnabled()) {
                    log.debug("Success acquire distribute lock[{}]", lockKey);
                }
                afterDistributedLock(distributedLimitListeners, methodMetadata, lockKey);

                Object result = joinPoint.proceed();

                afterExecute(result, distributedLimitListeners, methodMetadata, lockKey);
                return result;
            }

            @Override
            public Object onTimeout() {
                // 有自定义超时处理策略，执行自定义超时处理策略
                if (null != acquireLockTimeoutHandler) {
                    return acquireLockTimeoutHandler.onDistributedLockFailure(methodMetadata);
                }
                // 否则抛出默认异常
                DistributedLock distributedLock = methodMetadata.getAnnotation();
                String acquireTimeoutMessage = Optional.of(distributedLock.acquireTimeoutMessage())
                        .filter(StringUtils::hasLength)
                        .orElse(String.format("method [%s] acquire distributed lock fail", methodMetadata.getClassMethodName()));
                throw new AcquireLockTimeoutException(acquireTimeoutMessage);
            }
        };
    }

    /*  ---------------------- 执行监听器 ----------------------  */

    private void beforeDistributedLock(MethodMetadata methodMetadata, List<IDistributedLockListener> distributedLimitListeners,
                                       String lockKey) {
        if (CollectionUtils.isNotEmpty(distributedLimitListeners)) {
            distributedLimitListeners.forEach(distributedLimitListener ->
                    distributedLimitListener.beforeDistributedLock(methodMetadata, lockKey));
        }
    }

    private void afterDistributedLock(List<IDistributedLockListener> distributedLimitListeners, MethodMetadata methodMetadata,
                                      String lockKey) {
        if (CollectionUtils.isNotEmpty(distributedLimitListeners)) {
            distributedLimitListeners.forEach(distributedLimitListener ->
                    distributedLimitListener.afterDistributedLock(methodMetadata, lockKey));
        }
    }

    private void afterExecute(Object result, List<IDistributedLockListener> distributedLimitListeners,
                              MethodMetadata methodMetadata, String lockKey) {
        if (CollectionUtils.isNotEmpty(distributedLimitListeners)) {
            distributedLimitListeners.forEach(distributedLimitListener ->
                    distributedLimitListener.afterExecute(methodMetadata, lockKey, result));
        }
    }

    private void distributedLockFinally(MethodMetadata methodMetadata, List<IDistributedLockListener> distributedLimitListeners,
                                        String lockKey) {
        if (CollectionUtils.isNotEmpty(distributedLimitListeners)) {
            distributedLimitListeners.forEach(distributedLimitListener ->
                    distributedLimitListener.distributedLockFinally(methodMetadata, lockKey));
        }
    }

}
