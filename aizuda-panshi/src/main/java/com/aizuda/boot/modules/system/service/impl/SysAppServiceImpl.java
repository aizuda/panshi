/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysApp;
import com.aizuda.boot.modules.system.mapper.SysAppMapper;
import com.aizuda.boot.modules.system.service.ISysAppService;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 应用 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysAppServiceImpl extends BaseServiceImpl<SysAppMapper, SysApp> implements ISysAppService {

    @Override
    public Page<SysApp> page(Page<SysApp> page, SysApp sysApp) {
        LambdaQueryWrapper<SysApp> lqw = Wrappers.lambdaQuery(sysApp);
        return super.page(page, lqw);
    }

    @Override
    public boolean updateStatusById(Long id, Integer status) {
        SysApp sysApp = new SysApp();
        sysApp.setId(id);
        sysApp.setStatus(Objects.equals(status, 1) ? 1 : 0);
        return super.updateById(sysApp);
    }
}
