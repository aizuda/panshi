/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.io.InputStream;

/**
 * ClassLoader 工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class ClassLoaderUtils {

    /**
     * 项目 resources 中加载指定模板文件
     *
     * @param name 模板文件名，包含完整路径
     * @return {@link InputStream}
     */
    public static InputStream getResourceAsStream(String name) {
        return ClassLoaderUtils.class.getClassLoader().getResourceAsStream(name);
    }
}
