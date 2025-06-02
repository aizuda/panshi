/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.util.regex.Pattern;

/**
 * 正则表达式判断工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class RegexUtils {

    /**
     * 电子邮箱
     */
    public static String EMAIL = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    public static boolean isEmail(String email) {
        return matches(EMAIL, email);
    }

    /**
     * 判断是否为密码为6~20位数字,英文,符号至少两种组合的字符
     *
     * @param password 密码
     */
    public static boolean isPassword(String password) {
        return isAlphanumericSymbols(password, 6, 20);
    }

    /**
     * 判断数字,英文,符号至少两种组合的字符
     *
     * @param input 待判断字符串
     * @param min   最小长度
     * @param max   最大长度
     */
    public static boolean isAlphanumericSymbols(CharSequence input, int min, int max) {
        StringBuffer pwd = new StringBuffer();
        pwd.append("^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?![,\\.!#%'\\+\\*\\-:;^_`]+$)[,\\.!#%'\\+\\*\\-:;^_`0-9A-Za-z]{");
        pwd.append(min).append(",").append(max).append("}$");
        return matches(pwd.toString(), input);
    }

    /**
     * 字符串正则判断
     *
     * @param regex 正则
     * @param input 待判断字符串
     */
    public static boolean matches(String regex, CharSequence input) {
        return Pattern.matches(regex, input);
    }
}
