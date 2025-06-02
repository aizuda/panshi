package com.aizuda.boot.modules.gen.service.impl;

import com.aizuda.boot.modules.gen.entity.GenTemplate;
import com.aizuda.boot.modules.gen.mapper.GenTemplateMapper;
import com.aizuda.boot.modules.gen.service.IGenTemplateService;
import com.aizuda.common.toolkit.CollectionUtils;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代码生成模板表 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class GenTemplateServiceImpl extends BaseServiceImpl<GenTemplateMapper, GenTemplate> implements IGenTemplateService {

    @Override
    public Page<GenTemplate> page(Page<GenTemplate> page, GenTemplate genTemplate) {
        LambdaQueryWrapper<GenTemplate> lqw = Wrappers.lambdaQuery(genTemplate);
        return super.page(page, lqw);
    }

    @Override
    public List<GenTemplate> listAll() {
        return super.list();
    }

    @Override
    public List<GenTemplate> listCheckByIds(List<Long> ids) {
        List<GenTemplate> gts = super.listByIds(ids);
        ApiAssert.fail(CollectionUtils.isEmpty(gts), "请选择生成模板");
        return gts;
    }

    @Override
    public boolean updateById(GenTemplate genTemplate) {
        ApiAssert.fail(null == genTemplate.getId(), "主键不存在无法更新");
        return super.updateById(genTemplate);
    }
}
