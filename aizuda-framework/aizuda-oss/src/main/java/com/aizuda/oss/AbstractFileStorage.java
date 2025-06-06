/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.oss;

import com.aizuda.common.toolkit.DateUtils;
import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.common.toolkit.ThreadLocalUtils;
import com.aizuda.oss.autoconfigure.OssProperty;
import com.aizuda.oss.exception.MediaTypeException;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * aizuda 抽象文件存储类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public abstract class AbstractFileStorage implements IFileStorage {
    /**
     * 配置属性
     */
    protected OssProperty ossProperty;

    /**
     * 存储桶名称
     */
    protected String getBucketName() {
        String tempBucketName = this.tempBucketName();
        String _bucketName = ThreadLocalUtils.get(tempBucketName);
        if (null == _bucketName) {
            _bucketName = ossProperty.getBucketName();
        } else {
            // 释放临时桶名称
            ThreadLocalUtils.remove(tempBucketName);
        }
        return _bucketName;
    }

    /**
     * 临时桶名称
     */
    protected String tempBucketName() {
        return this.getClass().getName() + "OssBucket";
    }

    @Override
    public IFileStorage bucket(String bucketName) {
        if (StringUtils.hasLength(bucketName)) {
            ThreadLocalUtils.put(this.tempBucketName(), bucketName);
        }
        return this;
    }

    /**
     * 存储对象名称，默认生成日期文件路径，按年月目录存储
     *
     * @param suffix     文件后缀
     * @param objectName 文件对象名
     * @return 文件名，包含存储路径
     */
    protected String getObjectName(String suffix, String objectName) {
        if (null != objectName) {
            return objectName;
        }
        StringBuffer ojn = new StringBuffer();
        ojn.append(DateUtils.nowTimeFormat("yyyyMM")).append("/");
        ojn.append(UUID.randomUUID()).append(".").append(suffix);
        return ojn.toString();
    }

    @Override
    public IFileStorage allowMediaType(InputStream is, Function<String, Boolean> function) throws Exception {
        boolean legal = false;
        is.mark(is.available() + 1);
        String mediaType = MediaType.detect(is);
        if (null == function) {
            List<String> allowMediaType = this.ossProperty.getAllowMediaType();
            if (null != allowMediaType) {
                legal = allowMediaType.stream().anyMatch(t -> mediaType.startsWith(t));
            }
        } else {
            legal = function.apply(mediaType);
        }
        // 不合法媒体类型抛出异常
        if (!legal) {
            throw new MediaTypeException("Illegal file type");
        }
        is.reset();
        return this;
    }
}
