/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.exception;

import lombok.Getter;

/**
 * 不能获取到分布式锁异常，
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 1.1.0
 */
public class CannotAcquireLockException extends DistributedLockException {
    /**
     * 分布式锁的key
     */
    @Getter
    private final String lockKey;


    public CannotAcquireLockException(String lockKey, String message, Throwable cause) {
        super(message, cause);
        this.lockKey = lockKey;
    }
}
