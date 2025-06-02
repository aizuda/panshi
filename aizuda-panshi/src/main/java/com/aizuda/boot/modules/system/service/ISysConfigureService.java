/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysConfigure;
import com.aizuda.service.service.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 扩展配置 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysConfigureService extends IBaseService<SysConfigure> {

    Page<SysConfigure> page(Page<SysConfigure> page, SysConfigure sysConfigure);

}
