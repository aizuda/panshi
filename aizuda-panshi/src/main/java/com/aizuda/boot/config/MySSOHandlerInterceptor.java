package com.aizuda.boot.config;

import com.aizuda.core.api.ApiAssert;
import com.aizuda.core.api.ApiErrorCode;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.web.handler.SSOHandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class MySSOHandlerInterceptor implements SSOHandlerInterceptor {

    @Override
    public boolean preTokenIsNullAjax(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    @Override
    public boolean preTokenIsNull(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    @Override
    public boolean preToken(HttpServletRequest request, HttpServletResponse response, SSOToken ssoToken) {
        // 验证登录票据有效性
        ApiAssert.fail(ssoToken.timeExpired(), ApiErrorCode.TOKEN_EXPIRED);
        return true;
    }
}
