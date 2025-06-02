/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.core.api;

import com.aizuda.core.exception.ApiException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * REST API 业务断言
 * <p>参考：org.junit.Assert</p>
 *
 * @author 青苗
 * @since 1.1.0
 */
public class ApiAssert {

    protected ApiAssert() {
        // to do noting
    }

    /**
     * 失败结果
     *
     * @param errorCode 异常错误码
     */
    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

    public static void fail(boolean condition, IErrorCode errorCode) {
        if (condition) {
            fail(errorCode);
        }
    }

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(boolean condition, Supplier<String> supplier) {
        fail(condition, supplier.get());
    }

    public static void fail(boolean condition, String message) {
        if (condition) {
            fail(message);
        }
    }

    public static void isEmpty(Object obj, String message) {
        fail(ObjectUtils.isEmpty(obj), message);
    }

    public static void nonEmpty(Object obj, String message) {
        fail(!ObjectUtils.isEmpty(obj), message);
    }

    public static void equals(Object a, Object b, String message) {
        fail(Objects.equals(a, b), message);
    }

    public static void nonEquals(Object a, Object b, String message) {
        fail(!Objects.equals(a, b), message);
    }

    public static void fail(String message, MessageSource messageSource) {
        fail(messageSource.getMessage(message, null, LocaleContextHolder.getLocale()));
    }

    public static void fail(boolean condition, String message, MessageSource messageSource) {
        if (condition) {
            fail(message, messageSource);
        }
    }

    public static void fail(String message, Object[] args, MessageSource messageSource) {
        fail(messageSource.getMessage(message, args, LocaleContextHolder.getLocale()));
    }

    public static void fail(boolean condition, String message, Object[] args, MessageSource messageSource) {
        if (condition) {
            fail(message, args, messageSource);
        }
    }
}
