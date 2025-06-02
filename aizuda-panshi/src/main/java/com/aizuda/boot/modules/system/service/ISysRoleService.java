/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysRole;
import com.aizuda.boot.modules.system.entity.dto.SysRoleDTO;
import com.aizuda.service.service.IBaseService;
import com.aizuda.service.vo.TreeVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 系统角色 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysRoleService extends IBaseService<SysRole> {

    /**
     * 分页
     */
    Page<SysRole> page(Page<SysRole> page, SysRole sysRole);

    /**
     * 列表树
     */
    List<TreeVO> listTree();

    /**
     * 列表（显示所有角色）
     */
    List<SysRole> listAll();

    /**
     * 根据 DTO 修改角色信息
     */
    boolean updateResourceSet(SysRoleDTO dto);

    /**
     * 修改状态
     */
    boolean updateStatusById(Long id, Integer status);

    /**
     * 删除并检测是否允许操作
     *
     * @param ids 主键ID列表
     */
    boolean removeCheckByIds(List<Long> ids);
}
