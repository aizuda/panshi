/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysRole;
import com.aizuda.boot.modules.system.entity.dto.RoleResourceDTO;
import com.aizuda.boot.modules.system.entity.dto.SysRoleDTO;
import com.aizuda.boot.modules.system.service.ISysRoleResourceService;
import com.aizuda.boot.modules.system.service.ISysRoleService;
import com.aizuda.core.api.ApiController;
import com.aizuda.core.api.PageParam;
import com.aizuda.core.validation.Create;
import com.aizuda.core.validation.Update;
import com.aizuda.service.vo.TreeVO;
import com.baomidou.kisso.annotation.Permission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "系统角色")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/role")
public class SysRoleController extends ApiController {
    private ISysRoleService sysRoleService;
    private ISysRoleResourceService sysRoleResourceService;

    @Operation(summary = "分页列表")
    @Permission("sys:role:page")
    @PostMapping("/page")
    public Page<SysRole> getPage(@RequestBody PageParam<SysRole> dto) {
        return sysRoleService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "列表树")
    @Permission("sys:role:listTree")
    @GetMapping("/list-tree")
    public List<TreeVO> listTree() {
        return sysRoleService.listTree();
    }

    @Operation(summary = "列表（显示所有角色）")
    @Permission("sys:role:listAll")
    @GetMapping("/list-all")
    public List<SysRole> listAll() {
        return sysRoleService.listAll();
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:role:get")
    @GetMapping("/get")
    public SysRole get(@RequestParam Long id) {
        return sysRoleService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:role:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysRole sysRole) {
        return sysRoleService.updateById(sysRole);
    }

    @Operation(summary = "根据 id 修改信息设置权限")
    @Permission("sys:role:updateResourceSet")
    @PostMapping("/update-resource-set")
    public boolean updateResourceSet(@RequestBody SysRoleDTO dto) {
        return sysRoleService.updateResourceSet(dto);
    }

    @Operation(summary = "根据 id 修改状态")
    @Permission("sys:role:status")
    @PostMapping("/status/{id}")
    public boolean status(@PathVariable("id") Long id, @RequestParam Integer status) {
        return sysRoleService.updateStatusById(id, status);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:role:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysRole sysRole) {
        return sysRoleService.save(sysRole);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:role:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysRoleService.removeCheckByIds(ids);
    }

    @Operation(summary = "设置角色资源权限")
    @Permission("sys:role:resourceSet")
    @PostMapping("/resource-set")
    public boolean resourceSet(@Validated @RequestBody RoleResourceDTO dto) {
        return sysRoleResourceService.saveByRoleResourceParam(dto);
    }

    @Operation(summary = "查询角色资源权限ID列表")
    @Permission("sys:role:resourceIds")
    @GetMapping("/resource-ids")
    public List<Long> resourceIds(@RequestParam Long id) {
        return sysRoleResourceService.listByRoleId(id);
    }
}
