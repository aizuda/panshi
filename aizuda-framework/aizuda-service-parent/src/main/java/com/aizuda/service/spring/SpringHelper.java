/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * Spring Boot 相关辅助类
 *
 * @author 青苗
 * @since 1.1.0
 */
@SuppressWarnings("unused")
public class SpringHelper {
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 获取当前请求
     */
    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            // 这里不能抛出异常，存在为 null 的场景
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 设置 applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (null == APPLICATION_CONTEXT) {
            APPLICATION_CONTEXT = applicationContext;
        }
    }

    /**
     * 获取 applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * 通过class获取Bean
     *
     * @param clazz
     * @param <T>
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
