package com.aizuda.boot.modules.gen.service;

import com.aizuda.boot.modules.common.vo.SelectOptionVO;
import com.aizuda.boot.modules.gen.entity.GenDatabase;
import com.aizuda.service.service.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 代码生成数据源表 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface IGenDatabaseService extends IBaseService<GenDatabase> {

    Page<GenDatabase> page(Page<GenDatabase> page, GenDatabase genDatabase);

    GenDatabase getNoPasswordById(Long id);

    List<SelectOptionVO> listSelectOptions();
}
