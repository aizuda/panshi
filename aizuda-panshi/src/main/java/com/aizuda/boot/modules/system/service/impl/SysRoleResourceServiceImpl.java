/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysRoleResource;
import com.aizuda.boot.modules.system.entity.dto.RoleResourceDTO;
import com.aizuda.boot.modules.system.mapper.SysRoleResourceMapper;
import com.aizuda.boot.modules.system.service.ISysRoleResourceService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色资源 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysRoleResourceServiceImpl extends BaseServiceImpl<SysRoleResourceMapper, SysRoleResource> implements ISysRoleResourceService {

    @Override
    public boolean updateById(SysRoleResource sysRoleResource) {
        ApiAssert.isEmpty(sysRoleResource.getId(), "主键不存在无法更新");
        return super.updateById(sysRoleResource);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveByRoleResourceParam(RoleResourceDTO dto) {
        if (null == dto || null == dto.getRoleId()) {
            return false;
        }
        super.remove(Wrappers.<SysRoleResource>lambdaQuery().eq(SysRoleResource::getRoleId, dto.getRoleId()));
        if (CollectionUtils.isEmpty(dto.getResourceIds())) {
            // 执行角色权限清空操作，逻辑上返回成功
            return true;
        }
        return super.saveBatch(dto.getResourceIds().stream().map(resourceId -> {
            SysRoleResource srr = new SysRoleResource();
            srr.setRoleId(dto.getRoleId());
            srr.setResourceId(resourceId);
            return srr;
        }).toList());
    }

    @Override
    public List<Long> listByRoleId(Long roleId) {
        List<SysRoleResource> sysRoleResourceList = super.list(Wrappers.<SysRoleResource>lambdaQuery()
                .select(SysRoleResource::getResourceId).eq(SysRoleResource::getRoleId, roleId));
        return CollectionUtils.isEmpty(sysRoleResourceList) ? null : sysRoleResourceList.stream().map(SysRoleResource::getResourceId).toList();
    }

    @Override
    public boolean existRelByRoleId(Long roleId) {
        return lambdaQuery().eq(SysRoleResource::getRoleId, roleId).count() > 0;
    }

    @Override
    public boolean existRelByResourceIds(List<Long> resourceIds) {
        return lambdaQuery().in(SysRoleResource::getResourceId, resourceIds).count() > 0;
    }
}
