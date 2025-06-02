/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysUserRole;
import com.aizuda.boot.modules.system.entity.dto.AssignRolesDTO;
import com.aizuda.boot.modules.system.mapper.SysUserRoleMapper;
import com.aizuda.boot.modules.system.service.ISysUserRoleService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户角色 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean assignRoles(AssignRolesDTO dto) {
        // 删除历史
        List<Long> userIds = dto.getUserIds();
        this.removeByUserIds(userIds);
        if (CollectionUtils.isEmpty(dto.getRoleIds())) {
            // 无需分配角色
            return true;
        }

        // 批量新增
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        userIds.forEach(userId -> sysUserRoleList.addAll(dto.getRoleIds().stream()
                .map(roleId -> {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(userId);
                    sysUserRole.setRoleId(roleId);
                    return sysUserRole;
                }).toList()));
        return super.saveBatch(sysUserRoleList);
    }

    private boolean removeByUserIds(List<Long> userIds) {
        return super.remove(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, userIds));
    }

    @Override
    public boolean updateById(SysUserRole sysUserRole) {
        ApiAssert.isEmpty(sysUserRole.getId(), "主键不存在无法更新");
        return super.updateById(sysUserRole);
    }

    @Override
    public List<Long> listRoleIdsByUserId(Long userId) {
        List<SysUserRole> sysUserRoleList = super.list(Wrappers.<SysUserRole>lambdaQuery()
                .select(SysUserRole::getRoleId).eq(SysUserRole::getUserId, userId));
        return CollectionUtils.isEmpty(sysUserRoleList) ? null : sysUserRoleList.stream().map(SysUserRole::getRoleId).toList();
    }

    @Override
    public List<Long> listUserIdsByRoleIds(List<Long> roleIds) {
        List<SysUserRole> sysUserRoleList = super.list(Wrappers.<SysUserRole>lambdaQuery()
                .select(SysUserRole::getUserId).in(SysUserRole::getRoleId, roleIds));
        return CollectionUtils.isEmpty(sysUserRoleList) ? null : sysUserRoleList.stream().map(SysUserRole::getUserId).toList();
    }

    @Override
    public boolean existRelByRoleId(Long roleId) {
        return lambdaQuery().eq(SysUserRole::getRoleId, roleId).count() > 0;
    }

    @Override
    public boolean existRoles(Long userId, List<Long> roleIds) {
        return lambdaQuery().eq(SysUserRole::getUserId, userId)
                .in(SysUserRole::getRoleId, roleIds).count() > 0;
    }
}
