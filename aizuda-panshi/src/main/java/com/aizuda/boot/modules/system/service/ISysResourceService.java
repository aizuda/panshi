/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysResource;
import com.aizuda.boot.modules.system.entity.dto.ResourceDTO;
import com.aizuda.boot.modules.system.entity.vo.ResourceTreeVO;
import com.aizuda.service.service.IBaseService;
import com.aizuda.service.web.UserSession;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 系统资源 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysResourceService extends IBaseService<SysResource> {

    Page<SysResource> page(Page<SysResource> page, SysResource sysResource);

    /**
     * 树列表
     */
    List<ResourceTreeVO> listTree();

    /**
     * 权限菜单列表
     *
     * @param userSession {@link UserSession}
     */
    Map<String, Object> listMenuPermissions(UserSession userSession);

    /**
     * 更新菜单
     *
     * @param dto {@link ResourceDTO}
     */
    boolean updateByResourceParam(ResourceDTO dto);

    /**
     * 删除菜单
     */
    boolean removeByResourceIds(List<Long> ids);
}
