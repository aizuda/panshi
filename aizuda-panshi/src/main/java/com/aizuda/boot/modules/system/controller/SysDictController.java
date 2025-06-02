/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.common.vo.SelectOptionVO;
import com.aizuda.boot.modules.system.entity.SysDict;
import com.aizuda.boot.modules.system.service.ISysDictService;
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
 * 系统字典 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "系统字典")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/dict")
public class SysDictController extends ApiController {
    private ISysDictService sysDictService;

    @Operation(summary = "分页列表")
    @Permission("sys:dict:page")
    @PostMapping("/page")
    public Page<SysDict> getPage(@RequestBody PageParam<SysDict> dto) {
        return sysDictService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "父级字典列表")
    @Permission("sys:dict:listParent")
    @GetMapping("/list-parent")
    public List<SysDict> listParent() {
        return sysDictService.listParent();
    }

    @Operation(summary = "通过字典编码查询表单下拉选择项列表")
    @Permission("sys:dict:listSelectOptions")
    @GetMapping("/list-select-options")
    public List<SelectOptionVO> listSelectOptions(@RequestParam String code) {
        return sysDictService.listSelectOptions(code);
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:dict:get")
    @GetMapping("/get")
    public SysDict get(@RequestParam Long id) {
        return sysDictService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:dict:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysDict sysDict) {
        return sysDictService.updateById(sysDict);
    }

    @Operation(summary = "根据 id 修改状态")
    @Permission("sys:dict:status")
    @PostMapping("/status/{id}")
    public boolean status(@PathVariable("id") Long id, @RequestParam Integer status) {
        return sysDictService.updateStatusById(id, status);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:dict:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysDict sysDict) {
        return sysDictService.save(sysDict);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:dict:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysDictService.removeByIds(ids);
    }
}
