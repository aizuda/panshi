/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.log;

import com.aizuda.common.toolkit.ThrowableUtils;
import com.aizuda.core.exception.ApiException;
import com.aizuda.service.spring.SpringHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 操作日志拦截切面
 *
 * @author 青苗
 * @since 1.1.0
 */
@Aspect
public class OplogAspect {
    private IOplogStorageProvider oplogStorageProvider;

    public OplogAspect(IOplogStorageProvider oplogStorageProvider) {
        this.oplogStorageProvider = oplogStorageProvider;
    }

    /**
     * 配置环绕通知
     *
     * @param pjp {@link ProceedingJoinPoint}
     * @return
     * @throws Throwable
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object oplogAround(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Oplog oplog = OplogContext.builder().around(pjp.getSignature(),
                SpringHelper.getCurrentRequest(), pjp.getArgs());
        Object result;
        try {
            // 执行原方法
            result = pjp.proceed();
            // 执行成功
            oplog.status(true);
        } catch (Throwable t) {
            if (t instanceof ApiException) {
                // 逻辑异常
                oplog.content(t.getMessage()).failed();
            } else {
                // 系统异常
                oplog.exception(ThrowableUtils.getStackTrace(t));
            }
            throw t;
        } finally {
            // 保存操作日志
            oplogStorageProvider.save(pjp.getSignature(), oplog.build(System.currentTimeMillis() - beginTime));
            // 删除操作日志
            OplogContext.remove();
        }
        return result;
    }
}
