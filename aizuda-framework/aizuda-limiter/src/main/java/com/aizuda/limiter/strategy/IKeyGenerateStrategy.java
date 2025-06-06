/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.strategy;

import com.aizuda.common.toolkit.RequestUtils;
import com.aizuda.limiter.metadata.MethodMetadata;

import jakarta.servlet.http.HttpServletRequest;

/**
 * key生成策略接口
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public interface IKeyGenerateStrategy {

    /**
     * 策略类型
     *
     * @return 限流策略类型
     */
    String getType();

    /**
     * 唯一标示 key
     *
     * @param methodMetadata {@link MethodMetadata}
     * @param parseKey       解析spEL得到的Key
     * @return 包装的key
     */
    String getKey(MethodMetadata methodMetadata, String parseKey);

    /**
     * 当前请求
     *
     * @return 当前请求
     */
    default HttpServletRequest getRequest() {
        return RequestUtils.getRequest();
    }
}
