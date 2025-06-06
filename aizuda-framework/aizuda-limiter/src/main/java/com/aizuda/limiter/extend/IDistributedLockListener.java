/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.extend;


import com.aizuda.limiter.metadata.DistributedLockMethodMetaData;
import com.aizuda.limiter.metadata.MethodMetadata;

/**
 * 分布式锁扩展
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 1.1.0
 */
public interface IDistributedLockListener {

    /**
     * 判断是否支持当前监听
     *
     * @param methodMetadata 当前运行时参数 {@link DistributedLockMethodMetaData}
     * @return 是否支持
     */
    boolean supports(MethodMetadata methodMetadata);

    /**
     * 在加锁之前需要运行
     *
     * @param methodMetadata 当前方法的运行时参数 {@link DistributedLockMethodMetaData}
     * @param redisKey       redisKey
     */
    void beforeDistributedLock(MethodMetadata methodMetadata, String redisKey);

    /**
     * 在加锁之后还未执行用户方法时运行
     *
     * @param methodMetadata 当前方法的运行时参数 {@link DistributedLockMethodMetaData}
     * @param redisKey       redisKey
     */
    void afterDistributedLock(MethodMetadata methodMetadata, String redisKey);


    /**
     * 在加锁之后并运行用户方法后运行
     * 注意：此方法有可能因执行用户方法后异常不运行
     *
     * @param methodMetadata 当前方法的运行时参数 {@link DistributedLockMethodMetaData}
     * @param redisKey       redisKey
     * @param result         结果
     */
    void afterExecute(MethodMetadata methodMetadata, String redisKey, Object result);

    /**
     * 一定会运行的方法，可能还未加锁成功
     *
     * @param methodMetadata 当前方法的运行时参数 {@link DistributedLockMethodMetaData}
     * @param redisKey       redisKey
     */
    void distributedLockFinally(MethodMetadata methodMetadata, String redisKey);


}
