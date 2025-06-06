/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.metadata;


import com.aizuda.limiter.annotation.DistributedLock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 运行时的信息
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 1.1.0
 */
public interface MethodMetadata {

    /**
     * 获得当前运行时方法名
     * {@link Method}
     *
     * @return 运行时方法名
     */
    String getClassMethodName();

    /**
     * 获得当前运行时方法名
     *
     * @return 运行时方法
     */
    Method getMethod();

    /**
     * 获得运行时方法的参数
     *
     * @return 参数数组
     */
    Object[] getArgs();

    /**
     * 获得当前运行的参数
     *
     * @param <T> 当前运行的注解泛型，可能是{@link DistributedLock}
     * @return 获取到的泛型的值
     */
    <T extends Annotation> T getAnnotation();
}
