/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysPost;
import com.aizuda.boot.modules.system.mapper.SysPostMapper;
import com.aizuda.boot.modules.system.service.ISysPostService;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 岗位 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysPostServiceImpl extends BaseServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    @Override
    public Page<SysPost> page(Page<SysPost> page, SysPost sysPost) {
        LambdaQueryWrapper<SysPost> lqw = Wrappers.lambdaQuery(sysPost);
        return super.page(page, lqw);
    }

    @Override
    public boolean updateStatusById(Long id, Integer status) {
        SysPost sysPost = new SysPost();
        sysPost.setId(id);
        sysPost.setStatus(Objects.equals(status, 1) ? 1 : 0);
        return super.updateById(sysPost);
    }
}
