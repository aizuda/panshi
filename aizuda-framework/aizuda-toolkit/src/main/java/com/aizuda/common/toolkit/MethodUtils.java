/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 反射方法相关工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class MethodUtils {

    /**
     * 获取执行类方法名
     *
     * @param method 执行方法
     * @return 类名+方法名
     */
    public static String getClassMethodName(Method method) {
        return String.format("%s.%s", method.getDeclaringClass().getName(), method.getName());
    }

    /**
     * 获取方法指定注解信息
     *
     * @param method          {@link Method}
     * @param annotationClass 注解类
     * @param <T>             注解类
     * @return 指定注解信息
     */
    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }
}
