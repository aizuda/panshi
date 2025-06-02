/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class AnnotationUtils {

    /**
     * 获取类注解对象
     *
     * @param clazz           {@link Class}
     * @param annotationClass 类注解
     * @param <T>             注解泛型
     */
    public static <T extends Annotation> T get(Class<?> clazz, Class<T> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    /**
     * 获取方法注解对象
     *
     * @param method          {@link Method}
     * @param annotationClass 类注解
     * @param <T>             注解泛型
     */
    public static <T extends Annotation> T get(Method method, Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }
}
