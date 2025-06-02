/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.web;

import com.baomidou.kisso.SSOAuthorization;
import com.baomidou.kisso.web.handler.SSOHandlerInterceptor;
import com.baomidou.kisso.web.interceptor.SSOPermissionInterceptor;
import com.baomidou.kisso.web.interceptor.SSOSpringInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * Service Web 相关配置
 *
 * @author 青苗
 * @since 1.1.0
 */
@Slf4j
@AllArgsConstructor
public class ServiceWebMvcConfigurer implements WebMvcConfigurer {
    private SSOAuthorization ssoAuthorization;
    private IExcludePaths excludePaths;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // SSO 授权拦截器
        SSOSpringInterceptor ssoInterceptor = new SSOSpringInterceptor();
        ssoInterceptor.setHandlerInterceptor(new SSOHandlerInterceptor() {
            @Override
            public boolean preTokenIsNullAjax(HttpServletRequest request, HttpServletResponse response) {
                return false;
            }

            @Override
            public boolean preTokenIsNull(HttpServletRequest request, HttpServletResponse response) {
                return false;
            }
        });
        InterceptorRegistration registration = registry.addInterceptor(ssoInterceptor);
        registration.addPathPatterns("/**");

        // 权限拦截器
        SSOPermissionInterceptor permissionInterceptor = new SSOPermissionInterceptor();
        permissionInterceptor.setAuthorization(this.ssoAuthorization);
        InterceptorRegistration registrationPermission = registry.addInterceptor(permissionInterceptor);
        registrationPermission.addPathPatterns("/**");

        // 排除登录权限验证
        List<String> eps = new ArrayList<>();
        eps.add("/v3/api-docs/**");
        if (null != excludePaths) {
            eps.addAll(excludePaths.getPaths());
        }
        registration.excludePathPatterns(eps);
        registrationPermission.excludePathPatterns(eps);
    }
}
