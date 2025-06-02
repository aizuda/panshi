/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.common.vo.SelectOptionVO;
import com.aizuda.boot.modules.system.entity.SysDict;
import com.aizuda.service.service.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 系统字典 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysDictService extends IBaseService<SysDict> {

    Page<SysDict> page(Page<SysDict> page, SysDict sysDict);

    /**
     * 父级字典列表
     */
    List<SysDict> listParent();

    /**
     * 通过字典编码查询表单下拉选择项列表
     */
    List<SelectOptionVO> listSelectOptions(String code);

    /**
     * 修改状态
     */
    boolean updateStatusById(Long id, Integer status);

}
