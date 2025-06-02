/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.mapper;

import com.aizuda.boot.modules.system.entity.SysResourceApi;
import com.aizuda.service.mapper.CrudMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 系统资源接口 Mapper 接口
 * </p>
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface SysResourceApiMapper extends CrudMapper<SysResourceApi> {

    @Select("SELECT DISTINCT a.code FROM sys_resource_api a JOIN sys_role_resource s ON a.resource_id=s.resource_id JOIN sys_user_role r ON s.role_id=r.role_id WHERE r.user_id=#{userId}")
    List<String> selectCodesByUserId(Long userId);
}
