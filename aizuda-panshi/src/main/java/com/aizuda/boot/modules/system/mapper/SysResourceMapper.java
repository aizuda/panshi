/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.mapper;

import com.aizuda.boot.modules.system.entity.SysResource;
import com.aizuda.service.mapper.CrudMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 系统资源 Mapper 接口
 * </p>
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface SysResourceMapper extends CrudMapper<SysResource> {

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     */
    @Select("SELECT r.* FROM sys_resource r WHERE EXISTS (SELECT 1 FROM sys_role_resource s JOIN sys_user_role t ON s.role_id=t.role_id WHERE t.user_id=#{userId} AND r.id=s.resource_id) AND type<3 AND r.deleted=0 AND r.status=1 ORDER BY sort DESC")
    List<SysResource> selectMenuByUserId(Long userId);
}
