/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.oss;

import com.aizuda.common.toolkit.SpringUtils;
import com.aizuda.oss.autoconfigure.OssProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * aizuda 文件存储接口
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
@Slf4j
public class OSS {

    /**
     * 根据平台选择文件存储实现实例
     *
     * @param platform 存储平台，对应 yml 配置 map key
     * @return 文件存储实现实例 {@link IFileStorage}
     */
    public static IFileStorage fileStorage(String platform) {
        return SpringUtils.getBean(platform, IFileStorage.class);
    }

    /**
     * 根据平台选择文件存储实现实例
     *
     * @return 文件存储实现实例 {@link IFileStorage}
     */
    public static IFileStorage fileStorage() {
        return SpringUtils.getBean(OssProperties.getDefaultPlatform(), IFileStorage.class);
    }
}
