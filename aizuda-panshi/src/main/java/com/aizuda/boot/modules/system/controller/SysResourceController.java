/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysResource;
import com.aizuda.boot.modules.system.entity.SysResourceApi;
import com.aizuda.boot.modules.system.entity.dto.ResourceDTO;
import com.aizuda.boot.modules.system.entity.vo.ResourceTreeVO;
import com.aizuda.boot.modules.system.service.ISysResourceApiService;
import com.aizuda.boot.modules.system.service.ISysResourceService;
import com.aizuda.core.api.ApiController;
import com.aizuda.core.api.PageParam;
import com.aizuda.core.validation.Create;
import com.aizuda.core.validation.Update;
import com.aizuda.service.web.UserSession;
import com.baomidou.kisso.annotation.Permission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统资源 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "系统资源")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/resource")
public class SysResourceController extends ApiController {
    private ISysResourceService sysResourceService;
    private ISysResourceApiService sysResourceApiService;

    @Operation(summary = "分页列表")
    @Permission("sys:resource:page")
    @PostMapping("/page")
    public Page<SysResource> getPage(@RequestBody PageParam<SysResource> dto) {
        return sysResourceService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "树列表")
    @Permission("sys:resource:listTree")
    @GetMapping("/list-tree")
    public List<ResourceTreeVO> listTree() {
        return sysResourceService.listTree();
    }

    @Operation(summary = "接口列表")
    @Permission("sys:resource:listApi")
    @GetMapping("/list-api")
    public List<SysResourceApi> listApi(@RequestParam Long id) {
        return sysResourceApiService.listByResourceId(id);
    }

    @Operation(summary = "权限菜单列表")
    @Permission(ignore = true)
    @GetMapping("/list-menu-permissions")
    public Map<String, Object> listMenuPermissions() {
        return sysResourceService.listMenuPermissions(UserSession.getLoginInfo());
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:resource:get")
    @GetMapping("/get")
    public SysResource get(@RequestParam Long id) {
        return sysResourceService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:resource:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody ResourceDTO dto) {
        return sysResourceService.updateByResourceParam(dto);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:resource:create")
    @PostMapping("/create")
    public Long create(@Validated(Create.class) @RequestBody SysResource sysResource) {
        if (null == sysResource.getHidden()) {
            // 隐藏菜单
            sysResource.setHidden(false);
        }
        if (null == sysResource.getKeepAlive()) {
            // 保留查询参数
            sysResource.setKeepAlive(false);
        }
        return sysResourceService.save(sysResource) ? sysResource.getId() : null;
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:resource:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysResourceService.removeByResourceIds(ids);
    }
}
