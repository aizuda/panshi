/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysConfigure;
import com.aizuda.boot.modules.system.service.ISysConfigureService;
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
 * 扩展配置 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "扩展配置")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/configure")
public class SysConfigureController extends ApiController {
    private ISysConfigureService sysConfigureService;

    @Operation(summary = "分页列表")
    @Permission("sys:configure:page")
    @PostMapping("/page")
    public Page<SysConfigure> getPage(@RequestBody PageParam<SysConfigure> dto) {
        return sysConfigureService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:configure:get")
    @GetMapping("/get")
    public SysConfigure get(@RequestParam Long id) {
        return sysConfigureService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:configure:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysConfigure sysConfigure) {
        return sysConfigureService.updateById(sysConfigure);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:configure:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysConfigure sysConfigure) {
        return sysConfigureService.save(sysConfigure);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:configure:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysConfigureService.removeByIds(ids);
    }
}
