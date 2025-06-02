package com.aizuda.boot.modules.gen.service;

import com.aizuda.boot.modules.gen.entity.GenTemplate;
import com.aizuda.service.service.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 代码生成模板表 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface IGenTemplateService extends IBaseService<GenTemplate> {

    Page<GenTemplate> page(Page<GenTemplate> page, GenTemplate genTemplate);

    List<GenTemplate> listAll();

    List<GenTemplate> listCheckByIds(List<Long> ids);
}
