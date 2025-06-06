/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.security.handler;

import com.aizuda.security.autoconfigure.SecurityProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.lang.reflect.Type;

/**
 * 接口加密处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public interface IRestEncryptHandler {

    /**
     * 请求加密消息处理方法
     *
     * @param props         配置信息
     * @param inputMessage  请求加密消息
     * @param parameter     方法参数
     * @param targetType    {@link Type}
     * @param converterType {@link HttpMessageConverter}
     */
    HttpInputMessage request(SecurityProperties props, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                             Class<? extends HttpMessageConverter<?>> converterType);

    /**
     * 响应加密消息处理方法
     *
     * @param props                 配置信息
     * @param body                  返回对象
     * @param returnType            方法参数
     * @param selectedContentType   {@link MediaType}
     * @param selectedConverterType {@link HttpMessageConverter}
     * @param request               {@link ServerHttpRequest}
     * @param response              {@link ServerHttpResponse}
     */
    Object response(SecurityProperties props, Object body, MethodParameter returnType, MediaType selectedContentType,
                    Class<? extends HttpMessageConverter<?>> selectedConverterType,
                    ServerHttpRequest request, ServerHttpResponse response);

}
