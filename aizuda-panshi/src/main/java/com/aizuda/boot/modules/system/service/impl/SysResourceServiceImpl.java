/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysResource;
import com.aizuda.boot.modules.system.entity.dto.ResourceDTO;
import com.aizuda.boot.modules.system.entity.enums.ResourceType;
import com.aizuda.boot.modules.system.entity.vo.MenuVO;
import com.aizuda.boot.modules.system.entity.vo.ResourceTreeVO;
import com.aizuda.boot.modules.system.mapper.SysResourceMapper;
import com.aizuda.boot.modules.system.service.ISysResourceService;
import com.aizuda.boot.modules.system.service.ISysRoleResourceService;
import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.aizuda.service.web.UserSession;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统资源 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class SysResourceServiceImpl extends BaseServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {
    private ISysRoleResourceService sysRoleResourceService;

    @Override
    public Page<SysResource> page(Page<SysResource> page, SysResource sysResource) {
        LambdaQueryWrapper<SysResource> lqw = Wrappers.lambdaQuery(sysResource);
        return super.page(page, lqw);
    }

    private List<SysResource> listMenuAll() {
        return lambdaQuery().eq(SysResource::getStatus, 1)
                // 菜单不查按钮
                .ne(SysResource::getType, 3)
                .orderByDesc(SysResource::getSort).list();
    }

    @Override
    public List<ResourceTreeVO> listTree() {
        List<SysResource> sysResourceList = lambdaQuery().orderByDesc(SysResource::getSort).list();
        if (CollectionUtils.isEmpty(sysResourceList)) {
            return null;
        }
        return sysResourceList.stream().filter(e -> Objects.equals(0L, e.getPid())).map(e -> {
            ResourceTreeVO vo = e.convert(ResourceTreeVO.class);
            vo.setChildren(this.getChild(vo.getId(), vo.getTitle(), sysResourceList));
            return vo;
        }).toList();
    }

    /**
     * 获取子节点
     */
    protected List<ResourceTreeVO> getChild(Long id, String parentName, List<SysResource> sysResourceList) {
        // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
        List<SysResource> childList = sysResourceList.stream().filter(e -> Objects.equals(id, e.getPid())).toList();
        if (childList.isEmpty()) {
            // 没有子节点，返回一个空 List（递归退出）
            return null;
        }
        // 递归
        return childList.stream().map(e -> {
            ResourceTreeVO vo = e.convert(ResourceTreeVO.class);
            vo.setParentName(parentName);
            vo.setChildren(this.getChild(vo.getId(), vo.getTitle(), sysResourceList));
            return vo;
        }).toList();
    }

    @Override
    public Map<String, Object> listMenuPermissions(UserSession userSession) {
        Map<String, Object> menuMap = new HashMap<>(2);
        menuMap.put("menu", this.listMenuVO(userSession));
        menuMap.put("permissions", Arrays.asList("list.add", "list.edit", "list.delete",
                "user.add", "user.edit", "user.delete"));
        return menuMap;
    }

    protected List<MenuVO> listMenuVO(UserSession userSession) {
        List<SysResource> sysResourceList;
        if (UserSession.isAdmin(userSession.getId())) {
            // 管理员设置为所有权限
            sysResourceList = this.listMenuAll();
        } else {
            sysResourceList = baseMapper.selectMenuByUserId(userSession.getId());
        }
        if (CollectionUtils.isEmpty(sysResourceList)) {
            return Collections.EMPTY_LIST;
        }
        return sysResourceList.stream().filter(e -> Objects.equals(0L, e.getPid()))
                .map(e -> this.getMenuVO(e, sysResourceList))
                .toList();
    }

    protected MenuVO getMenuVO(SysResource sysResource, List<SysResource> sysResourceList) {
        MenuVO vo = new MenuVO();
        vo.setName(sysResource.getAlias());
        vo.setRedirect(sysResource.getRedirect());
        vo.setPath(sysResource.getPath());
        vo.setComponent(sysResource.getComponent());
        vo.setMeta(new HashMap<>(5) {{
            put("title", sysResource.getTitle());
            put("icon", sysResource.getIcon());
            put("type", ResourceType.convert(sysResource.getType()));
            if (sysResource.getHidden()) {
                put("hidden", true);
            }
            put("parentRoute", sysResource.getParentRoute());
            put("order", sysResource.getSort());
            final String query = sysResource.getQuery();
            if (null != query) {
                put("query", JacksonUtils.readMap(query).entrySet().stream().map(e ->
                        Map.of("key", e.getKey(), "value", e.getValue())).toList());
            }
        }});
        vo.setChildren(this.getMenuChild(sysResource.getId(), sysResourceList));
        return vo;
    }

    /**
     * 获取子节点
     */
    protected List<MenuVO> getMenuChild(Long id, List<SysResource> sysResourceList) {
        // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
        List<SysResource> childList = sysResourceList.stream().filter(e -> Objects.equals(id, e.getPid())).toList();
        if (childList.isEmpty()) {
            // 没有子节点，返回一个空 List（递归退出）
            return null;
        }
        // 递归
        return childList.stream().map(e -> this.getMenuVO(e, sysResourceList)).toList();
    }

    @Override
    public boolean removeByResourceIds(List<Long> ids) {
        this.checkExists(Wrappers.<SysResource>lambdaQuery().select(SysResource::getId)
                .in(SysResource::getPid, ids), "存在子类不允许删除");
        ApiAssert.fail(sysRoleResourceService.existRelByResourceIds(ids), "已分配角色不允许删除");
        return super.removeByIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByResourceParam(ResourceDTO dto) {
        ApiAssert.isEmpty(dto.getId(), "主键不存在无法更新");
        SysResource sysResource = dto.convert(SysResource.class);
        ApiAssert.fail(!super.updateById(sysResource), "更新失败");
        return true;
    }
}
