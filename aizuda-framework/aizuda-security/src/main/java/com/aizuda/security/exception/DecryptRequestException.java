/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.security.exception;

import com.aizuda.common.toolkit.ObjectUtils;

/**
 * 请求参数解密签名异常
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class DecryptRequestException extends RuntimeException {

    public DecryptRequestException(String message) {
        super(message);
    }

    public DecryptRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void isEmpty(Object object, String message) {
        if (ObjectUtils.isEmpty(object)) {
            throw new DecryptRequestException(message);
        }
    }
}
