/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysPost;
import com.aizuda.service.service.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 岗位 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysPostService extends IBaseService<SysPost> {

    Page<SysPost> page(Page<SysPost> page, SysPost sysPost);

    /**
     * 修改状态
     */
    boolean updateStatusById(Long id, Integer status);

}
