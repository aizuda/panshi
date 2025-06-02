package com.aizuda.common.toolkit;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Stream操作工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author VampireAchao
 * @since 1.1.0
 */
public class StreamUtils {

    /**
     * 封装最常用的map函数
     *
     * @param list     集合
     * @param function 你想要进行的操作，需要带返回值，lambda写法例如：{@code i-> i.getId()或者 User::getId }
     * @param <O>      原来的集合中元素类型
     * @param <R>      map之后的集合中元素类型
     * @return 执行了map后的集合
     */
    public static <O, R> List<R> map(List<O> list, Function<O, R> function) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(function).toList();
    }

    /**
     * 封装了最常用的map函数的变种peek函数
     *
     * @param list     集合
     * @param function 你想要进行的操作
     * @param <E>      集合中元素类型
     * @return 执行了peek后的集合
     */
    public static <E> List<E> peek(List<E> list, Consumer<E> function) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().peek(function).toList();
    }

    /**
     * 封装了使用Stream过滤集合的方法
     *
     * @param list      集合
     * @param predicate 过滤的条件，lambda写法例如：{@code i-> i.getId() != null }
     * @param <E>       集合中元素类型
     * @return 过滤后的集合
     */
    public static <E> List<E> filter(List<E> list, Predicate<E> predicate) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().filter(predicate).toList();
    }

    /**
     * 通过指定字段去重
     *
     * @param list     集合
     * @param function 你想要进行的操作，需要带返回值
     *                 lambda写法例如：{@code i-> i.getId()或者 User::getId }
     *                 多个字段写法例如：{@code i-> i.getId() + ";" + i.getName() }
     * @return {@link List}<{@link T}> 去重后的集合
     */
    public static <T, U extends Comparable<? super U>> List<T> distinct(List<T> list, Function<T, U> function) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(function))), ArrayList::new));
    }

    /**
     * list转map
     * {@code List<实体>} 转为 {@code Map<实体属性,实体>}
     *
     * @param list        集合
     * @param keyFunction 作为key的lambda
     * @param <E>         集合类型
     * @param <K>         key的类型
     * @return {@code Map<实体属性,实体>}
     */
    public static <E, K> Map<K, E> toMap(List<E> list, Function<E, K> keyFunction) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(HashMap::new, (m, v) -> m.put(Optional.ofNullable(v).map(keyFunction).orElse(null), v), HashMap::putAll);
    }


    /**
     * 通过实体属性分组
     * {@code List<实体>} 转为 {@code Map<实体属性,List<实体>>}
     *
     * @param list        集合
     * @param keyFunction 作为key的lambda
     * @param <E>         集合类型
     * @param <K>         key的类型
     * @return {@code Map<实体属性,List<实体>>}
     */
    public static <E, K> Map<K, List<E>> group(List<E> list, Function<E, K> keyFunction) {
        return groupThen(list, keyFunction, Collectors.toList());
    }

    /**
     * 通过实体属性分组，还能继续后续操作
     * {@code List<实体>} 转为 {@code Map<实体属性,?>}
     *
     * @param list        集合
     * @param keyFunction 作为key的lambda
     * @param downstream  可能你还想进行别的操作
     * @param <E>         集合类型
     * @param <K>         key的类型
     * @param <R>         你想进行的后续操作类型
     * @return {@code Map<实体属性,?>}
     */
    public static <E, K, R> Map<K, R> groupThen(List<E> list, Function<E, K> keyFunction, Collector<E, ?, R> downstream) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(keyFunction, downstream));
    }
}
