package com.aizuda.cache.autoconfigure;

import com.aizuda.cache.RedisTemplateCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Redis 配置加载
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "aizuda.redis.enabled", havingValue = "true", matchIfMissing = true)
public class RedisAutoConfiguration {

    private final CacheProperties.RedisConfig redisConfig;

    public RedisAutoConfiguration(CacheProperties cacheProperties) {
        this.redisConfig = cacheProperties.getRedis();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return switch (redisConfig.getMode().toLowerCase()) {
            case "cluster" -> createClusterConnection();
            case "sentinel" -> createSentinelConnection();
            default -> createStandaloneConnection();
        };
    }

    private RedisConnectionFactory createStandaloneConnection() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisConfig.getHost());
        config.setPort(redisConfig.getPort());
        config.setPassword(redisConfig.getPassword());
        config.setDatabase(redisConfig.getDatabase());
        return new LettuceConnectionFactory(config);
    }

    private RedisConnectionFactory createClusterConnection() {
        RedisClusterConfiguration config = new RedisClusterConfiguration(redisConfig.getNodes());
        config.setPassword(redisConfig.getPassword());
        return new LettuceConnectionFactory(config);
    }

    private RedisConnectionFactory createSentinelConnection() {
        RedisSentinelConfiguration config = new RedisSentinelConfiguration();
        config.setMaster(redisConfig.getSentinelMaster());
        config.setSentinels(getSentinels());
        config.setPassword(redisConfig.getPassword());
        return new LettuceConnectionFactory(config);
    }

    private Set<RedisNode> getSentinels() {
        Set<RedisNode> nodes = new HashSet<>();
        for (String sentinel : redisConfig.getSentinels()) {
            String[] parts = sentinel.split(":");
            nodes.add(new RedisNode(parts[0], Integer.parseInt(parts[1])));
        }
        return nodes;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisObjectTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build(), ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplateCache redisTemplateCache(RedisTemplate<String, Object> redisObjectTemplate) {
        return new RedisTemplateCache(redisObjectTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);

        // 设置默认缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(redisConfig.getCacheConfigs().get("defaultCache").getTtl()))
                .prefixCacheNameWith(redisConfig.getCacheConfigs().get("defaultCache").getKeyPrefix());
        builder.cacheDefaults(defaultCacheConfig);

        // 设置其它缓存配置
        Map<String, CacheProperties.RedisCacheConfig> cacheConfigs = redisConfig.getCacheConfigs();
        cacheConfigs.forEach((cacheName, config) -> {
            RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(config.getTtl()))
                    .prefixCacheNameWith(config.getKeyPrefix());
            builder.withCacheConfiguration(cacheName, cacheConfig);
        });

        return builder.build();
    }
}
