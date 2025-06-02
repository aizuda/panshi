/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysUser;
import com.aizuda.boot.modules.system.entity.dto.ResetPasswordDTO;
import com.aizuda.boot.modules.system.entity.dto.SysUserDTO;
import com.aizuda.boot.modules.system.entity.vo.SysUserRelIdsVO;
import com.aizuda.boot.modules.system.entity.vo.SysUserVO;
import com.aizuda.service.service.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 系统用户 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysUserService extends IBaseService<SysUser> {

    Page<SysUser> page(Page<SysUser> page, SysUserVO vo);

    /**
     * 查询满足 20 条记录的用户列表
     */
    List<SysUser> list20ByUsername(String username);

    boolean save(SysUserDTO dto);

    boolean updateById(SysUserDTO dto);

    /**
     * 修改用户状态
     */
    boolean updateStatusById(Long id, Integer status);

    boolean resetPassword(ResetPasswordDTO dto);

    SysUser getById(Long id);

    /**
     * 根据 ID 获取关联角色部门ID列表
     */
    SysUserRelIdsVO getRelIdsById(Long id);

}
