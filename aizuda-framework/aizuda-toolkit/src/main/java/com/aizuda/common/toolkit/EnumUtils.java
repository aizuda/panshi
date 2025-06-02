package com.aizuda.common.toolkit;

import java.util.*;
import java.util.function.Function;

/**
 * 枚举工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author gwml
 * @since 1.1.0
 */
public class EnumUtils {

    private static final Map<Class<Enum>, LinkedHashMap<Object, Enum>> ENUM_CACHE = new HashMap<>();

    /**
     * 从缓存中获取枚举map
     * @param clazz 枚举class
     * @param keyFunction 获取存入缓存键的函数
     */
    public static <K, E extends Enum> LinkedHashMap<K, E> getCacheEnumMap(Class<E> clazz, Function<E, K> keyFunction) {
        if (ENUM_CACHE.containsKey(clazz)) {
            return (LinkedHashMap<K, E>) ENUM_CACHE.get(clazz);
        }
        LinkedHashMap<K, E> enumMap = getEnumMap(clazz, keyFunction);
        ENUM_CACHE.put((Class<Enum>) clazz, (LinkedHashMap<Object, Enum>) enumMap);
        return enumMap;
    }

    /**
     * 获得枚举Map
     * @param clazz  枚举class
     * @param keyFunction 获取键的函数
     */
    public static <K, E extends Enum> LinkedHashMap<K, E> getEnumMap(Class<E> clazz, Function<E, K> keyFunction) {
        LinkedHashMap<K, E> map = new LinkedHashMap();
        E[] values = clazz.getEnumConstants();
        for (E e : values) {
            map.put(keyFunction.apply(e), e);
        }
        return map;
    }

    /**
     * 获得枚举名称为键的枚举Map
     * @param clazz  枚举class
     */
    public static <String, E extends Enum<E>> LinkedHashMap<String, E> getEnumMap(Class<E> clazz) {
        return (LinkedHashMap<String, E>) getEnumMap(clazz, x -> x.name());
    }

    /**
     * 根据名称获取枚举
     */
    public static <E extends Enum<E>> E get(Class<E> clazz, String name) {
        return get(clazz, name, x -> x.name());
    }

    /**
     * 根据自定义键函数获取枚举
     * @param key 键
     * @param keyFunction 键函数
     */
    public static <K, E extends Enum<E>> E get(Class<E> clazz, K key, Function<E, K> keyFunction) {
        LinkedHashMap<K, E> enumMap = getEnumMap(clazz, keyFunction);
        return enumMap.getOrDefault(key, null);
    }

    /**
     * 从缓存中获取枚举
     * @param key 键
     * @param keyFunction 获取键的函数
     */
    public static <K, E extends Enum> E getByCache(Class<E> clazz, K key, Function<E, K> keyFunction) {
        LinkedHashMap<K, E> cacheEnumMap = getCacheEnumMap(clazz, keyFunction);
        return cacheEnumMap.get(key);
    }

    /**
     * 判断枚举名称在枚举中是否存在
     */
    public static <E extends Enum<E>> boolean contains(Class<E> enumClass, String val) {
        return get(enumClass, val) != null;
    }

    /**
     * 获得枚举在枚举类中的下标
     */
    public static <E extends Enum<E>> int indexOf(E e) {
        List<? extends Enum> enums = Arrays.asList(e.getClass().getEnumConstants());
        return enums.indexOf(e);
    }


    public static <E extends Enum<E>> String toString(E e) {
        return toString(e, x -> x.name());
    }


    public static <E extends Enum<E>> String toString(E e, Function<E, String> valueFunction) {
        return null != e ? valueFunction.apply(e) : null;
    }
}
