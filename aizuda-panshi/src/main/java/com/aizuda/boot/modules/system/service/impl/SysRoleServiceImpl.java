/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysRole;
import com.aizuda.boot.modules.system.entity.dto.RoleResourceDTO;
import com.aizuda.boot.modules.system.entity.dto.SysRoleDTO;
import com.aizuda.boot.modules.system.mapper.SysRoleMapper;
import com.aizuda.boot.modules.system.service.ISysRoleResourceService;
import com.aizuda.boot.modules.system.service.ISysRoleService;
import com.aizuda.boot.modules.system.service.ISysUserRoleService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.aizuda.service.vo.TreeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 系统角色 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    private ISysUserRoleService sysUserRoleService;
    private ISysRoleResourceService sysRoleResourceService;

    @Override
    public Page<SysRole> page(Page<SysRole> page, SysRole sysRole) {
        LambdaQueryWrapper<SysRole> lqw = Wrappers.lambdaQuery(sysRole);
        return super.page(page, lqw);
    }

    @Override
    public List<TreeVO> listTree() {
        return super.list().stream().map(t -> new TreeVO(t.getId(),
                t.getName(), null)).toList();
    }

    @Override
    public List<SysRole> listAll() {
        return super.list();
    }

    @Override
    public boolean updateResourceSet(SysRoleDTO dto) {
        SysRole sysRole = dto.convert(SysRole.class);
        if (null == sysRole.getId()) {
            ApiAssert.fail(!super.save(sysRole), "角色信息保存失败");
        } else {
            ApiAssert.fail(!super.updateById(sysRole), "角色信息修改失败");
        }

        // 更新角色资源权限
        RoleResourceDTO rrd = new RoleResourceDTO();
        rrd.setRoleId(sysRole.getId());
        rrd.setResourceIds(dto.getResourceIds());
        return sysRoleResourceService.saveByRoleResourceParam(rrd);
    }

    @Override
    public boolean updateById(SysRole sysRole) {
        ApiAssert.isEmpty(sysRole.getId(), "主键不存在无法更新");
        return super.updateById(sysRole);
    }

    @Override
    public boolean updateStatusById(Long id, Integer status) {
        SysRole sysRole = new SysRole();
        sysRole.setId(id);
        sysRole.setStatus(Objects.equals(status, 1) ? 1 : 0);
        return super.updateById(sysRole);
    }

    @Override
    public boolean removeCheckByIds(List<Long> ids) {
        ids.forEach(id -> {
            ApiAssert.fail(sysUserRoleService.existRelByRoleId(id), "存在角色关联用户不允许删除");
            ApiAssert.fail(sysRoleResourceService.existRelByRoleId(id), "存在角色关联菜单权限不允许删除");
        });
        return super.removeByIds(ids);
    }
}
