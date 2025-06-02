/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class DateUtils {

    public interface Pattern {
        String p1 = "yyyy 年 MM 月 dd 日";
        String p2 = "yyyy-MM-dd HH:mm:ss";
        String p3 = "yyyy-MM-dd HH:mm";
        String p4 = "yyyy-MM-dd HH";
        String p5 = "yyyy-MM-dd";

    }

    public static String nowTime() {
        return nowTimeFormat(Pattern.p2);
    }

    public static String nowTimeFormat(String pattern) {
        return LocalDateTime.now().format(ofPattern(pattern));
    }

    public static DateTimeFormatter ofPattern(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * LocalDateTime转时间格式字符串
     *
     * @param localDateTime 时间
     * @param pattern       格式化
     * @return string
     */
    public static String formatToString(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(ofPattern(pattern));
    }
}
