/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import org.springframework.lang.Nullable;

import java.util.Arrays;

/**
 * 字符串工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class StringUtils {

    /**
     * 判断是否为非空字符串
     *
     * @param str 字符串
     */
    public static boolean hasLength(@Nullable String str) {
        return null != str && !str.isEmpty();
    }


    /**
     * 判断是否全部为非空字符串
     * str不能是空的 或者长度为0
     * @param str 字符串
     */
    public static boolean allHasLength(@Nullable String... str) {
       return str != null && str.length != 0 &&  Arrays.stream(str).allMatch(StringUtils::hasLength);
    }

    /**
     * 判断是否存在非空字符串
     * str不能是空的 或者长度为0
     * @param str 字符串
     */
    public static boolean anyHasLength(@Nullable String... str) {
        return str != null && str.length != 0 &&  Arrays.stream(str).anyMatch(StringUtils::hasLength);
    }



    /**
     * 按字节截取字符串
     *
     * @param str   待截取字符串
     * @param bytes 字节长度
     */
    public static String substringByBytes(String str, int bytes) {
        if (hasLength(str)) {
            int len = 0;
            int strLength = str.length();
            for (int i = 0; i < strLength; i++) {
                // GBK 编码格式 中文占两个字节 UTF-8 编码格式中文占三个字节;
                len += (str.charAt(i) > 255 ? 3 : 1);
                if (len > bytes) {
                    return str.substring(0, i);
                }
            }
        }
        return str;
    }
}
