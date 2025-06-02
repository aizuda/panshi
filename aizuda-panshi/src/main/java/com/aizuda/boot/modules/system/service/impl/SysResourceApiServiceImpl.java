/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysResourceApi;
import com.aizuda.boot.modules.system.mapper.SysResourceApiMapper;
import com.aizuda.boot.modules.system.service.ISysResourceApiService;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 系统资源接口 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysResourceApiServiceImpl extends BaseServiceImpl<SysResourceApiMapper, SysResourceApi> implements ISysResourceApiService {

    @Override
    public List<SysResourceApi> listByResourceId(Long resourceId) {
        return super.list(Wrappers.<SysResourceApi>lambdaQuery().eq(SysResourceApi::getResourceId, resourceId));
    }

    @Override
    public Long saveReturnId(SysResourceApi sra) {
        boolean result = super.save(sra);
        if (result) {
            this.invalidateAuthAllCache();
            return sra.getId();
        }
        return null;
    }

    @Override
    public List<String> listCodesByUserId(Long userId) {
        return baseMapper.selectCodesByUserId(userId);
    }

    @Override
    public boolean updateById(SysResourceApi sra) {
        boolean result = super.updateById(sra);
        if (result) {
            this.invalidateAuthAllCache();
        }
        return result;
    }

    @Override
    public boolean removeByResourceIds(List<Long> ids) {
        boolean result = super.removeByIds(ids);
        if (result) {
            this.invalidateAuthAllCache();
        }
        return result;
    }

    /**
     * 用户权限编码列表缓存 15 分钟
     */
    protected Cache<Long, List<String>> codeListCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    /**
     * 用户权限编码校验缓存 5 分钟
     */
    protected Cache<String, Boolean> codeCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(6000)
            .build();

    @Override
    public boolean isPermitted(Long userId, String permission) {
        final String key = userId + permission;
        Boolean legalKey = codeCache.getIfPresent(key);
        if (null != legalKey) {
            return legalKey;
        }

        List<String> codeList = codeListCache.getIfPresent(userId);
        if (CollectionUtils.isEmpty(codeList)) {
            codeList = this.listCodesByUserId(userId);
        }
        if (CollectionUtils.isEmpty(codeList)) {
            codeCache.put(key, false);
            return false;
        } else {
            // 缓存用户权限编码
            codeListCache.put(userId, codeList);
        }

        // 判断当前权限编码是否存在
        legalKey = codeList.stream().anyMatch(t -> Objects.equals(t, permission)
                || permission.contains(t));
        codeCache.put(key, legalKey);
        return legalKey;
    }

    /**
     * 清除权限缓存
     */
    public void invalidateAuthAllCache() {
        codeCache.invalidateAll();
        codeListCache.invalidateAll();
    }
}
