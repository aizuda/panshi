/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * IO 工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class IoUtils {

    /**
     * 输入流写入输出流
     *
     * @param is {@link InputStream}
     * @param os {@link OutputStream}
     * @throws IOException
     */
    public static void write(InputStream is, OutputStream os) throws IOException {
        if (null != is && null != os) {
            byte[] buffer = new byte[2048];
            int length;
            while (-1 != (length = is.read(buffer))) {
                os.write(buffer, 0, length);
            }
        }
    }

    /**
     * 关闭流对象
     *
     * @param cls 可关闭的流对象列表
     * @return IOException
     */
    public static IOException close(Closeable... cls) {
        IOException exception = null;
        for (Closeable closeable : cls) {
            exception = close(closeable);
            if (null != exception) {
                break;
            }
        }
        return exception;
    }

    /**
     * 关闭流对象
     *
     * @param closeableColl 可关闭的流对象列表
     * @return IOException
     */
    public static IOException close(Collection<? extends Closeable> closeableColl) {
        if (CollectionUtils.isEmpty(closeableColl)) {
            return null;
        }
        IOException exception = null;
        for (Closeable closeable : closeableColl) {
            exception = close(closeable);
            if (null != exception) {
                break;
            }
        }
        return exception;
    }

    /**
     * 关闭流对象
     *
     * @param closeable 可关闭的流对象
     * @return IOException
     */
    public static IOException close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                return e;
            }
        }
        return null;
    }
}
