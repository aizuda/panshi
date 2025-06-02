/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysConfigure;
import com.aizuda.boot.modules.system.mapper.SysConfigureMapper;
import com.aizuda.boot.modules.system.service.ISysConfigureService;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

/**
 * 扩展配置 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysConfigureServiceImpl extends BaseServiceImpl<SysConfigureMapper, SysConfigure> implements ISysConfigureService {

    @Override
    public Page<SysConfigure> page(Page<SysConfigure> page, SysConfigure sysConfigure) {
        LambdaQueryWrapper<SysConfigure> lqw = Wrappers.lambdaQuery(sysConfigure);
        return super.page(page, lqw);
    }
}
