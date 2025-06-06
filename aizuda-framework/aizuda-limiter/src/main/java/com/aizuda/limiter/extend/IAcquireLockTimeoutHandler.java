/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.extend;


import com.aizuda.limiter.metadata.DistributedLockMethodMetaData;
import com.aizuda.limiter.metadata.MethodMetadata;

/**
 * 分布式锁获取超时后自定义处理策略
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 1.1.0
 */
public interface IAcquireLockTimeoutHandler {

    /**
     * 判断是否支持当前处理策略
     *
     * @param methodMetadata 当前运行时参数 {@link DistributedLockMethodMetaData}
     * @return 是否支持
     */
    boolean supports(MethodMetadata methodMetadata);

    /**
     * 在分布式锁获取超时失败时的处理策略
     *
     * @param methodMetadata 当前运行时参数 {@link DistributedLockMethodMetaData}
     * @return 返回结果
     */
    Object onDistributedLockFailure(MethodMetadata methodMetadata);
}
