/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 反射工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class ReflectUtils {

    /**
     * 创建 class 实例
     *
     * @param clazz Class
     * @param <T>   泛型对象
     * @return 待转换对象
     * @throws Exception
     */
    public static <T> T newInstance(Class clazz) throws Exception {
        return (T) clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * 创建 class 实例
     *
     * @param className Object.class.getName()
     * @param <T>       泛型对象
     * @return 待转换对象
     * @throws Exception
     */
    public static <T> T newInstance(String className) throws Exception {
        return (T) newInstance(Class.forName(className));
    }
    
    /**
     * 根据属性名及属性值组成的键值对灵活为对象赋值
     *
     * @param <T>      泛型对象
     * @param fieldMap 属性名及属性值组成的键值对
     * @return 赋值完成后对象
     * @throws Exception
     */
    public static <T> T flexSetField(T instance, Map<String, Object> fieldMap) throws Exception {
        Set<String> fieldNameSet = fieldMap.keySet();
        Class<?> memberVariablesClass = instance.getClass();
        for (String fieldName : fieldNameSet) {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, memberVariablesClass);
            Method writeMethod = propertyDescriptor.getWriteMethod();
            writeMethod.invoke(instance, fieldMap.get(fieldName));
        }
        return instance;
    }
}
