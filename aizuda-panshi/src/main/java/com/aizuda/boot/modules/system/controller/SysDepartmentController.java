/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysDepartment;
import com.aizuda.boot.modules.system.entity.vo.SysDepartmentVO;
import com.aizuda.boot.modules.system.service.ISysDepartmentService;
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
 * 部门 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "部门")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/department")
public class SysDepartmentController extends ApiController {
    private ISysDepartmentService sysDepartmentService;

    @Operation(summary = "分页列表")
    @Permission("sys:department:page")
    @PostMapping("/page")
    public Page<SysDepartment> getPage(@RequestBody PageParam<SysDepartment> dto) {
        return sysDepartmentService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "树列表")
    @Permission("sys:department:listTree")
    @PostMapping("/list-tree")
    public List<SysDepartmentVO> listTree(@RequestBody SysDepartment sysDepartment) {
        return sysDepartmentService.listTree(sysDepartment);
    }

    @Operation(summary = "列表（显示所有部门）")
    @Permission("sys:department:listAll")
    @GetMapping("/list-all")
    public List<SysDepartment> listAll() {
        return sysDepartmentService.listAll();
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:department:get")
    @GetMapping("/get")
    public SysDepartment get(@RequestParam Long id) {
        return sysDepartmentService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:department:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysDepartment sysDepartment) {
        return sysDepartmentService.updateById(sysDepartment);
    }

    @Operation(summary = "根据 id 修改状态")
    @Permission("sys:department:status")
    @PostMapping("/status/{id}")
    public boolean status(@PathVariable("id") Long id, @RequestParam Integer status) {
        return sysDepartmentService.updateStatusById(id, status);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:department:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysDepartment sysDepartment) {
        return sysDepartmentService.save(sysDepartment);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:department:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysDepartmentService.removeByIds(ids);
    }
}
