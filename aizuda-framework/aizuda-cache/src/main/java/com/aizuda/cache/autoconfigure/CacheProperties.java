package com.aizuda.cache.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = CacheProperties.OSS_PREFIX)
public class CacheProperties {

    public static final String OSS_PREFIX = "aizuda";

    private RedisConfig redis;

    @Getter
    @Setter
    public static class RedisConfig {
        // 是否启用
        private boolean enabled = true;
        // 默认单机模式，可选值 single 单机 cluster 集群 sentinel 哨兵
        private String mode = "single";
        private String host = "localhost";
        private int port = 6379;
        private String password;
        private int database = 0;

        // 集群节点
        private List<String> nodes;

        // 哨兵主节点
        private String sentinelMaster;
        private List<String> sentinels;

        // 缓存配置
        private Map<String, RedisCacheConfig> cacheConfigs;

    }

    @Getter
    @Setter
    public static class RedisCacheConfig {
        private Long ttl = 600L; // TTL in seconds (default 10 minutes)
        private String keyPrefix; // Cache key prefix

    }
}
