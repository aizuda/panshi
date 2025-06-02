/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysRegion;
import com.aizuda.boot.modules.system.entity.vo.SysRegionVO;
import com.aizuda.boot.modules.system.mapper.SysRegionMapper;
import com.aizuda.boot.modules.system.service.ISysRegionService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 行政区域 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysRegionServiceImpl extends BaseServiceImpl<SysRegionMapper, SysRegion> implements ISysRegionService {

    @Override
    public List<SysRegionVO> listTree(SysRegion sysRegion) {
        LambdaQueryWrapper<SysRegion> lqw = Wrappers.lambdaQuery();
        // pid 为 0 只查询省份城市信息
        if (Objects.equals(sysRegion.getPid(), 0L)) {
            lqw.in(SysRegion::getLevel, Arrays.asList(0, 1));
            sysRegion.setPid(null);
        }
        lqw.setEntity(sysRegion);
        List<SysRegion> sysRegionList = super.list(lqw);
        if (CollectionUtils.isEmpty(sysRegionList)) {
            return null;
        }
        return sysRegionList.stream().filter(e -> Objects.equals(0L, e.getPid())).map(e -> {
            SysRegionVO vo = e.convert(SysRegionVO.class);
            vo.setChildren(this.getChild(vo.getId(), vo.getName(), sysRegionList));
            return vo;
        }).toList();
    }

    /**
     * 获取子节点
     */
    protected List<SysRegionVO> getChild(Long id, String parentName, List<SysRegion> sysRegionList) {
        // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
        List<SysRegion> childList = sysRegionList.stream().filter(e -> Objects.equals(id, e.getPid())).toList();
        if (childList.isEmpty()) {
            // 没有子节点，返回一个空 List（递归退出）
            return null;
        }
        // 递归
        return childList.stream().map(e -> {
            SysRegionVO vo = e.convert(SysRegionVO.class);
            vo.setParentName(parentName);
            vo.setChildren(this.getChild(vo.getId(), vo.getName(), sysRegionList));
            return vo;
        }).toList();
    }

    @Override
    public boolean updateById(SysRegion sysRegion) {
        this.updateRegionLevel(sysRegion);
        return super.updateById(sysRegion);
    }

    private void updateRegionLevel(SysRegion sysRegion) {
        // 类型 0、省份直辖市 1、地市 2、区县
        if (null == sysRegion.getPid() || Objects.equals(0L, sysRegion.getPid())) {
            sysRegion.setLevel(0);
        } else {
            SysRegion parentRegion = this.checkById(sysRegion.getPid());
            ApiAssert.fail(2 == parentRegion.getLevel(), "区县为最小单位不允许再分");
            sysRegion.setLevel(parentRegion.getLevel() + 1);
        }
    }

    @Override
    public boolean save(SysRegion sysRegion) {
        this.updateRegionLevel(sysRegion);
        return super.save(sysRegion);
    }
}
