/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Spring 工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class SpringUtils {
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * <p>
     * 获取当前请求
     * </p>
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
     * <p>
     * 设置 applicationContext
     * </p>
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (null == APPLICATION_CONTEXT) {
            APPLICATION_CONTEXT = applicationContext;
        }
    }

    /**
     * <p>
     * 获取 applicationContext
     * </p>
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * <p>
     * 通过class获取Bean
     * </p>
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

    /**
     * 动态注入单例bean实例
     *
     * @param beanName        {@link ConfigurableApplicationContext}
     * @param beanName        bean名称
     * @param singletonObject 单例bean实例
     * @return 注入实例
     */
    public static Object registerSingletonBean(String beanName, Object singletonObject) {
        ApplicationContext applicationContext = getApplicationContext();
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton(beanName, singletonObject);
        return context.getBean(beanName);
    }
}
