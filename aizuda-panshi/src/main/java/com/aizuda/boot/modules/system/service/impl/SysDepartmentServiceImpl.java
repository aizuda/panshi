/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysDepartment;
import com.aizuda.boot.modules.system.entity.vo.DepartmentHeadVO;
import com.aizuda.boot.modules.system.entity.vo.SysDepartmentVO;
import com.aizuda.boot.modules.system.mapper.SysDepartmentMapper;
import com.aizuda.boot.modules.system.service.ISysDepartmentService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 部门 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysDepartmentServiceImpl extends BaseServiceImpl<SysDepartmentMapper, SysDepartment> implements ISysDepartmentService {

    @Override
    public Page<SysDepartment> page(Page<SysDepartment> page, SysDepartment sysDepartment) {
        LambdaQueryWrapper<SysDepartment> lqw = Wrappers.lambdaQuery(sysDepartment);
        return super.page(page, lqw);
    }

    @Override
    public List<SysDepartmentVO> listTree(SysDepartment sysDepartment) {
        List<SysDepartment> sysDepartmentList = super.list(Wrappers.lambdaQuery(sysDepartment));
        if (CollectionUtils.isEmpty(sysDepartmentList)) {
            return null;
        }
        return sysDepartmentList.stream().filter(e -> Objects.equals(0L, e.getPid())).map(e -> {
            SysDepartmentVO vo = e.convert(SysDepartmentVO.class);
            vo.setChildren(this.getChild(vo.getId(), vo.getName(), sysDepartmentList));
            return vo;
        }).toList();
    }

    /**
     * 获取子节点
     */
    private List<SysDepartmentVO> getChild(Long id, String parentName, List<SysDepartment> sysDepartmentList) {
        // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
        List<SysDepartment> childList = sysDepartmentList.stream().filter(e -> Objects.equals(id, e.getPid())).toList();
        if (childList.isEmpty()) {
            // 没有子节点，返回一个空 List（递归退出）
            return null;
        }
        // 递归
        return childList.stream().map(e -> {
            SysDepartmentVO vo = e.convert(SysDepartmentVO.class);
            vo.setParentName(parentName);
            vo.setChildren(this.getChild(vo.getId(), vo.getName(), sysDepartmentList));
            return vo;
        }).toList();
    }

    @Override
    public List<SysDepartment> listAll() {
        return super.list();
    }

    @Override
    public boolean updateById(SysDepartment sysDepartment) {
        ApiAssert.isEmpty(sysDepartment.getId(), "主键不存在无法更新");
        List<Long> ids = baseMapper.selectIdsRecursive(sysDepartment.getId());
        ApiAssert.fail(CollectionUtils.isNotEmpty(ids) && ids.contains(sysDepartment.getPid()),
                "父分类不能为子分类，请重新选择父分类");
        if (null == sysDepartment.getPid()) {
            // 未设置父ID设置为0
            sysDepartment.setPid(0L);
        }
        return super.updateById(sysDepartment);
    }

    @Override
    public boolean updateStatusById(Long id, Integer status) {
        SysDepartment sysDepartment = new SysDepartment();
        sysDepartment.setId(id);
        sysDepartment.setStatus(Objects.equals(status, 1) ? 1 : 0);
        return super.updateById(sysDepartment);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        this.checkExists(Wrappers.<SysDepartment>lambdaQuery().select(SysDepartment::getId)
                .in(SysDepartment::getPid, list), "存在子类不允许删除");
        return super.removeByIds(list);
    }

    @Override
    public List<DepartmentHeadVO> getDepartmentHeadInfo(Long userId, Integer examineLevel, boolean multiLevel) {
        DepartmentHeadVO vo = baseMapper.selectDepartmentHeadByUserId(userId);
        return null == vo ? null : Collections.singletonList(vo);
    }
}
