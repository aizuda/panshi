/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysUserRole;
import com.aizuda.boot.modules.system.entity.dto.AssignRolesDTO;
import com.aizuda.service.service.IBaseService;

import java.util.List;

/**
 * 系统用户角色 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysUserRoleService extends IBaseService<SysUserRole> {

    /**
     * 用户角色分配
     *
     * @param dto 分配角色参数对象
     */
    boolean assignRoles(AssignRolesDTO dto);

    /**
     * 根据用户ID查询关联角色ID列表
     *
     * @param userId 用户ID
     */
    List<Long> listRoleIdsByUserId(Long userId);

    /**
     * 根据角色ID列表查询关联用户ID列表
     *
     * @param roleIds 角色ID列表
     */
    List<Long> listUserIdsByRoleIds(List<Long> roleIds);

    /**
     * 判断是否存在关联角色
     *
     * @param roleId 角色ID
     */
    boolean existRelByRoleId(Long roleId);

    /**
     * 判断用户是否拥有角色
     */
    boolean existRoles(Long userId, List<Long> roleIds);
}
