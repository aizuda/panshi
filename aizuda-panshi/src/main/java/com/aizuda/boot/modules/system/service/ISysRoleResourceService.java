/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysRoleResource;
import com.aizuda.boot.modules.system.entity.dto.RoleResourceDTO;
import com.aizuda.service.service.IBaseService;

import java.util.List;

/**
 * 系统角色资源 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysRoleResourceService extends IBaseService<SysRoleResource> {

    /**
     * 保存角色资源菜单ID关联信息
     *
     * @param dto {@link RoleResourceDTO}
     */
    boolean saveByRoleResourceParam(RoleResourceDTO dto);

    /**
     * 查询指定角色ID列表
     *
     * @param roleId 角色ID
     */
    List<Long> listByRoleId(Long roleId);

    /**
     * 判断是否存在关联角色
     *
     * @param roleId 角色ID
     */
    boolean existRelByRoleId(Long roleId);

    /**
     * 判断是否存在角色关联资源
     *
     * @param resourceIds 资源ID列表
     */
    boolean existRelByResourceIds(List<Long> resourceIds);
}
