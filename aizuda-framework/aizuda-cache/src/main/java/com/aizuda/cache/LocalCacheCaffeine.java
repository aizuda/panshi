package com.aizuda.cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存 Caffeine
 */
public class LocalCacheCaffeine<K, V> {

    private Cache<K, V> cache;

    public LocalCacheCaffeine() {
        // 初始化缓存，设置默认配置
        this.cache = Caffeine.newBuilder()
                // 默认 10 分钟过期
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 默认最大缓存 1000 条
                .maximumSize(1000)
                .build();
    }

    // 初始化时可以设置不同的过期时间和最大缓存大小
    public LocalCacheCaffeine(long maxSize, long duration, TimeUnit unit) {
        this.cache = Caffeine.newBuilder()
                // 设置过期时间
                .expireAfterWrite(duration, unit)
                // 设置最大缓存条目
                .maximumSize(maxSize)
                .build();
    }

    // 获取缓存
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    // 设置缓存
    public void set(K key, V value) {
        cache.put(key, value);
    }

    // 设置缓存并设置过期时间
    public void set(K key, V value, long duration, TimeUnit unit) {
        cache.put(key, value);
        cache.policy().expireAfterWrite().ifPresent(expiry -> expiry.setExpiresAfter(duration, unit));
    }

    // 获取或加载缓存（如果缓存不存在时，使用提供的函数加载缓存）
    public V getOrLoad(K key, java.util.function.Function<? super K, ? extends V> loader) {
        return cache.get(key, loader);
    }

    // 删除缓存
    public void remove(K key) {
        cache.invalidate(key);
    }

    // 清空缓存
    public void clear() {
        cache.invalidateAll();
    }

    // 获取缓存的当前大小
    public long size() {
        return cache.estimatedSize();
    }

    // 判断缓存中是否包含指定的 key
    public boolean containsKey(K key) {
        return cache.getIfPresent(key) != null;
    }

    // 设置缓存的过期时间
    public void setExpireAfterWrite(long duration, TimeUnit unit) {
        this.cache = Caffeine.newBuilder()
                // 设置过期时间
                .expireAfterWrite(duration, unit)
                // 设置最大缓存条目
                .maximumSize(1000)
                .build();
    }
}
