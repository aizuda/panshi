/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.oss;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;

/**
 * oss 媒体类型辅助类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class MediaType {
    private static Tika TIKA;

    public static Tika getTika() {
        return TIKA;
    }

    /**
     * 提取媒体类型
     *
     * @param is 文件流 {@link InputStream}
     * @return 媒体类型
     */
    public static String detect(InputStream is) throws IOException {
        if (null == TIKA) {
            synchronized (MediaType.class) {
                TIKA = new Tika();
            }
        }
        return TIKA.detect(is);
    }
}
