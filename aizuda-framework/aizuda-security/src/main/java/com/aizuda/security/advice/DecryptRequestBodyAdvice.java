/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.security.advice;

import com.aizuda.security.annotation.Encrypted;
import com.aizuda.security.autoconfigure.SecurityProperties;
import com.aizuda.security.handler.IRestEncryptHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 接口请求解密处理切点
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
@Slf4j
@RestControllerAdvice
public class DecryptRequestBodyAdvice extends RequestBodyAdviceAdapter {
    private final SecurityProperties securityProperties;
    private final IRestEncryptHandler restEncryptHandler;

    public DecryptRequestBodyAdvice(SecurityProperties securityProperties, IRestEncryptHandler restEncryptHandler) {
        this.securityProperties = securityProperties;
        this.restEncryptHandler = restEncryptHandler;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> clazz;
        if (targetType instanceof ParameterizedType) {
            clazz = (Class<?>) ((ParameterizedType) targetType).getRawType();
        } else {
            clazz = (Class) targetType;
        }
        return Encrypted.class.isAssignableFrom(clazz);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        return restEncryptHandler.request(securityProperties, inputMessage, parameter, targetType, converterType);
    }
}
