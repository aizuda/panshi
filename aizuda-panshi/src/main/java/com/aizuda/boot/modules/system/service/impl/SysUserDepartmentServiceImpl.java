/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysUserDepartment;
import com.aizuda.boot.modules.system.entity.dto.AssignDepartmentsDTO;
import com.aizuda.boot.modules.system.mapper.SysUserDepartmentMapper;
import com.aizuda.boot.modules.system.service.ISysUserDepartmentService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户部门 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysUserDepartmentServiceImpl extends BaseServiceImpl<SysUserDepartmentMapper, SysUserDepartment> implements ISysUserDepartmentService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean assignDepartments(AssignDepartmentsDTO dto) {
        // 删除历史
        List<Long> userIds = dto.getUserIds();
        this.removeByUserIds(userIds);
        if (CollectionUtils.isEmpty(dto.getDepartmentIds())) {
            // 无需分配部门
            return true;
        }

        // 批量新增
        List<SysUserDepartment> sysUserDepartmentList = new ArrayList<>();
        userIds.forEach(userId -> sysUserDepartmentList.addAll(dto.getDepartmentIds().stream()
                .map(DepartmentId -> {
                    SysUserDepartment sysUserDepartment = new SysUserDepartment();
                    sysUserDepartment.setUserId(userId);
                    sysUserDepartment.setDepartmentId(DepartmentId);
                    return sysUserDepartment;
                }).toList()));
        return super.saveBatch(sysUserDepartmentList);
    }

    private boolean removeByUserIds(List<Long> userIds) {
        return super.remove(Wrappers.<SysUserDepartment>lambdaQuery().in(SysUserDepartment::getUserId, userIds));
    }

    @Override
    public boolean updateById(SysUserDepartment sysUserDepartment) {
        ApiAssert.isEmpty(sysUserDepartment.getId(), "主键不存在无法更新");
        return super.updateById(sysUserDepartment);
    }

    @Override
    public List<Long> listDepartmentIdsByUserId(Long userId) {
        List<SysUserDepartment> sysUserDepartmentList = super.list(Wrappers.<SysUserDepartment>lambdaQuery()
                .select(SysUserDepartment::getDepartmentId).eq(SysUserDepartment::getUserId, userId));
        return CollectionUtils.isEmpty(sysUserDepartmentList) ? null : sysUserDepartmentList.stream().map(SysUserDepartment::getDepartmentId).toList();
    }

    @Override
    public boolean existRelByDepartmentId(Long DepartmentId) {
        return lambdaQuery().eq(SysUserDepartment::getDepartmentId, DepartmentId).count() > 0;
    }
}
