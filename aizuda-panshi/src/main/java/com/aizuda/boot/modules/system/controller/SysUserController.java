/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysUser;
import com.aizuda.boot.modules.system.entity.dto.AssignDepartmentsDTO;
import com.aizuda.boot.modules.system.entity.dto.AssignRolesDTO;
import com.aizuda.boot.modules.system.entity.dto.ResetPasswordDTO;
import com.aizuda.boot.modules.system.entity.dto.SysUserDTO;
import com.aizuda.boot.modules.system.entity.vo.SysUserRelIdsVO;
import com.aizuda.boot.modules.system.entity.vo.SysUserVO;
import com.aizuda.boot.modules.system.service.ISysUserDepartmentService;
import com.aizuda.boot.modules.system.service.ISysUserRoleService;
import com.aizuda.boot.modules.system.service.ISysUserService;
import com.aizuda.core.api.ApiController;
import com.aizuda.core.api.PageParam;
import com.aizuda.core.validation.Create;
import com.aizuda.core.validation.Update;
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
 * 系统用户 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "系统用户")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/user")
public class SysUserController extends ApiController {
    private ISysUserService sysUserService;
    private ISysUserRoleService sysUserRoleService;
    private ISysUserDepartmentService sysUserDepartmentService;

    @Operation(summary = "分页列表")
    @Permission("sys:user:page")
    @PostMapping("/page")
    public Page<SysUser> getPage(@RequestBody PageParam<SysUserVO> dto) {
        return sysUserService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "查询满足条件20条记录的用户列表")
    @Permission(ignore = true)
    @PostMapping("/list20")
    public List<SysUser> list20ByUsername(@RequestParam(required = false) String username) {
        return sysUserService.list20ByUsername(username);
    }

    @Operation(summary = "当前登录用户信息")
    @Permission(ignore = true)
    @GetMapping("/info")
    public SysUser info() {
        return sysUserService.getById(null);
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:user:get")
    @GetMapping("/get")
    public SysUser get(@RequestParam Long id) {
        return sysUserService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:user:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysUserDTO dto) {
        return sysUserService.updateById(dto);
    }

    @Operation(summary = "根据 id 修改状态")
    @Permission("sys:user:status")
    @PostMapping("/status/{id}")
    public boolean status(@PathVariable("id") Long id, @RequestParam Integer status) {
        return sysUserService.updateStatusById(id, status);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:user:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysUserDTO dto) {
        return sysUserService.save(dto);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:user:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysUserService.removeByIds(ids);
    }

    @Operation(summary = "分配角色")
    @Permission("sys:user:assignRoles")
    @PostMapping("/assign-roles")
    public boolean assignRoles(@Validated @RequestBody AssignRolesDTO dto) {
        return sysUserRoleService.assignRoles(dto);
    }

    @Operation(summary = "分配部门")
    @Permission("sys:user:assignDepartments")
    @PostMapping("/assign-departments")
    public boolean assignDepartments(@Validated @RequestBody AssignDepartmentsDTO dto) {
        return sysUserDepartmentService.assignDepartments(dto);
    }

    @Operation(summary = "根据用户ID查询关联角色部门ID列表")
    @Permission("sys:user:relIds")
    @PostMapping("/rel-ids")
    public SysUserRelIdsVO relIds(@RequestParam Long id) {
        return sysUserService.getRelIdsById(id);
    }

    @Operation(summary = "重置密码")
    @Permission("sys:user:resetPassword")
    @PostMapping("/reset-password")
    public boolean resetPassword(@Validated @RequestBody ResetPasswordDTO param) {
        return sysUserService.resetPassword(param);
    }
}
