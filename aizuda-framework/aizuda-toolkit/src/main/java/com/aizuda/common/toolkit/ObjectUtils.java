/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 对象工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class ObjectUtils {

    /**
     * 判断object是否为空,集合会校验size
     */
    public static boolean isNull(Object... objs) {
        for (Object obj : objs) {
            if (isEmpty(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断object是否不为空,集合会校验size
     */
    public static boolean isNotNull(Object... obj) {
        return !isNull(obj);
    }

    /**
     * 对象非空判断
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 对象空判断
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof Iterable) {
            return !((Iterable<?>) obj).iterator().hasNext();
        }
        if (obj instanceof Iterator) {
            return !((Iterator<?>) obj).hasNext();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        // else
        return false;
    }

    /**
     * 对象转为字节数组
     *
     * @param obj 待转字节数组对象
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] toByteArray(Object obj) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(obj);
        return outputStream.toByteArray();
    }

    /**
     * 字节数组转化为泛型T对象
     *
     * @param byteArray 字节数组
     * @param <T>       泛型T对象
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T readObject(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream oInputStream = new ObjectInputStream(inputStream);
        return (T) oInputStream.readObject();
    }
}
