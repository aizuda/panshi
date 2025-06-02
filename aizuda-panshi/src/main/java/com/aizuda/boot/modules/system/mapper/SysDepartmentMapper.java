/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.mapper;

import com.aizuda.boot.modules.system.entity.SysDepartment;
import com.aizuda.boot.modules.system.entity.vo.DepartmentHeadVO;
import com.aizuda.service.mapper.CrudMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * </p>
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface SysDepartmentMapper extends CrudMapper<SysDepartment> {

    /**
     * 根据 id 递归子类集合
     */
    List<Long> selectIdsRecursive(@Param("id") Long id);

    /**
     * 查询用户的部门主管信息
     */
    DepartmentHeadVO selectDepartmentHeadByUserId(@Param("userId") Long userId);
}
