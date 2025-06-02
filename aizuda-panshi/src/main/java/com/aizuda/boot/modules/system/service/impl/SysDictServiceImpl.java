/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.common.vo.SelectOptionVO;
import com.aizuda.boot.modules.system.entity.SysDict;
import com.aizuda.boot.modules.system.mapper.SysDictMapper;
import com.aizuda.boot.modules.system.service.ISysDictService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 系统字典 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public Page<SysDict> page(Page<SysDict> page, SysDict sysDict) {
        LambdaQueryWrapper<SysDict> lqw = Wrappers.lambdaQuery(sysDict);
        return super.page(page, lqw);
    }

    @Override
    public List<SysDict> listParent() {
        return super.list(Wrappers.<SysDict>lambdaQuery().select(SysDict::getId, SysDict::getName, SysDict::getCode,
                SysDict::getRemark, SysDict::getStatus, SysDict::getSort).eq(SysDict::getPid, 0));
    }

    @Override
    public List<SelectOptionVO> listSelectOptions(String code) {
        List<SelectOptionVO> sovList = new ArrayList<>();
        List<SysDict> dictList = lambdaQuery().eq(SysDict::getCode, code).orderByDesc(SysDict::getSort).list();
        if (CollectionUtils.isNotEmpty(dictList)) {
            if (Objects.equals(1, dictList.size())) {
                // 单个字典配置情况且为一级字典，查询子集字典
                dictList = lambdaQuery().eq(SysDict::getPid, dictList.get(0).getId()).orderByDesc(SysDict::getSort).list();
            }
            // 表单下拉选择项列表转换
            sovList = dictList.stream().map(t -> SelectOptionVO.of(t.getName(), t.getContent())).toList();
        }
        return sovList;
    }

    @Override
    public boolean save(SysDict sysDict) {
        this.checkCode(sysDict);
        return super.save(sysDict);
    }

    /**
     * 编码重发校验
     */
    private void checkCode(SysDict sysDict) {
        checkExists(Wrappers.<SysDict>lambdaQuery().select(SysDict::getId)
                .ne(null != sysDict.getId(), SysDict::getId, sysDict.getId())
                .eq(SysDict::getPid, sysDict.getPid())
                .eq(SysDict::getCode, sysDict.getCode()), "编码已存在，请勿重复添加");
    }

    @Override
    public boolean updateById(SysDict sysDict) {
        ApiAssert.isEmpty(sysDict.getId(), "主键不存在无法更新");
        this.checkCode(sysDict);
        return super.updateById(sysDict);
    }

    @Override
    public boolean updateStatusById(Long id, Integer status) {
        SysDict sysDict = new SysDict();
        sysDict.setId(id);
        sysDict.setStatus(Objects.equals(status, 1) ? 1 : 0);
        return super.updateById(sysDict);
    }

    @Override
    public boolean removeByIds(Collection<?> ids) {
        this.checkExists(Wrappers.<SysDict>lambdaQuery().select(SysDict::getId)
                .in(SysDict::getPid, ids), "字典分类存在子类，请先删除子类");
        return super.removeByIds(ids);
    }
}
