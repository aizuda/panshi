/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysResourceApi;
import com.aizuda.service.service.IBaseService;

import java.util.List;

/**
 * 系统资源接口 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysResourceApiService extends IBaseService<SysResourceApi> {

    /**
     * 根据资源ID查询接口权限列表
     *
     * @param resourceId 资源ID
     */
    List<SysResourceApi> listByResourceId(Long resourceId);

    Long saveReturnId(SysResourceApi sra);

    /**
     * 根据用户ID查询权限编码列表
     *
     * @param userId 用户ID
     */
    List<String> listCodesByUserId(Long userId);

    boolean removeByResourceIds(List<Long> ids);

    /**
     * 用户权限校验
     *
     * @param userId     用户ID
     * @param permission 权限编码
     * @return true 验证通过 false 验证失败
     */
    boolean isPermitted(Long userId, String permission);
}
