package com.aizuda.service.advice;

import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.core.api.ApiResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应方法返回值处理类
 *
 * @author 青苗
 * @since 1.1.0
 */
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !returnType.getMethod().getReturnType().isAssignableFrom(Void.TYPE);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ApiResult) {
            return body;
        }
        ApiResult apiResult = ApiResult.ok(body);
        if (returnType.getParameterType().isAssignableFrom(String.class)) {
            // 字符串类型特殊处理
            return JacksonUtils.toJSONString(apiResult);
        }
        return apiResult;
    }
}
