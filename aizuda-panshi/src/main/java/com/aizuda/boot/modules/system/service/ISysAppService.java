/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysApp;
import com.aizuda.service.service.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 应用 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysAppService extends IBaseService<SysApp> {

    Page<SysApp> page(Page<SysApp> page, SysApp sysApp);

    /**
     * 修改状态
     */
    boolean updateStatusById(Long id, Integer status);

}
