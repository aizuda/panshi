/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysUserDepartment;
import com.aizuda.boot.modules.system.entity.dto.AssignDepartmentsDTO;
import com.aizuda.service.service.IBaseService;

import java.util.List;

/**
 * 系统用户部门 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysUserDepartmentService extends IBaseService<SysUserDepartment> {

    /**
     * 用户部门分配
     *
     * @param dto 分配部门参数对象
     */
    boolean assignDepartments(AssignDepartmentsDTO dto);

    /**
     * 根据用户ID查询关联部门ID列表
     *
     * @param userId 用户ID
     */
    List<Long> listDepartmentIdsByUserId(Long userId);

    /**
     * 判断是否存在关联部门
     *
     * @param DepartmentId 部门ID
     */
    boolean existRelByDepartmentId(Long DepartmentId);
}
