package com.aizuda.limiter.strategy;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.limiter.metadata.MethodMetadata;

/**
 * 默认key策略
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 风尘
 * @since 1.1.0
 */
public class DefaultKeyGenerateStrategy implements IKeyGenerateStrategy {
    public final static String TYPE = "aizuda-default";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getKey(MethodMetadata methodMetadata, String parseKey) {
        String result = methodMetadata.getClassMethodName();
        if (StringUtils.hasLength(parseKey)) {
            result += ":" + parseKey;
        }
        return result;
    }
}
