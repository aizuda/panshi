/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysDepartment;
import com.aizuda.boot.modules.system.entity.vo.DepartmentHeadVO;
import com.aizuda.boot.modules.system.entity.vo.SysDepartmentVO;
import com.aizuda.service.service.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 部门 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysDepartmentService extends IBaseService<SysDepartment> {

    Page<SysDepartment> page(Page<SysDepartment> page, SysDepartment sysDepartment);

    List<SysDepartmentVO> listTree(SysDepartment sysDepartment);

    /**
     * 列表（显示所有部门）
     */
    List<SysDepartment> listAll();

    /**
     * 获取部门主管信息
     *
     * @param userId       用户ID
     * @param examineLevel 指定主管层级
     * @param multiLevel   连续多级主管
     */
    List<DepartmentHeadVO> getDepartmentHeadInfo(Long userId, Integer examineLevel, boolean multiLevel);

    /**
     * 修改状态
     */
    boolean updateStatusById(Long id, Integer status);

}
