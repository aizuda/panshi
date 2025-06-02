/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysApp;
import com.aizuda.boot.modules.system.service.ISysAppService;
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
 * 应用 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "应用")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/app")
public class SysAppController extends ApiController {
    private ISysAppService sysAppService;

    @Operation(summary = "分页列表")
    @Permission("sys:app:page")
    @PostMapping("/page")
    public Page<SysApp> getPage(@RequestBody PageParam<SysApp> dto) {
        return sysAppService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:app:get")
    @GetMapping("/get")
    public SysApp get(@RequestParam Long id) {
        return sysAppService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:app:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysApp sysApp) {
        return sysAppService.updateById(sysApp);
    }

    @Operation(summary = "根据 id 修改状态")
    @Permission("sys:app:status")
    @PostMapping("/status/{id}")
    public boolean status(@PathVariable("id") Long id, @RequestParam Integer status) {
        return sysAppService.updateStatusById(id, status);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:app:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysApp sysApp) {
        return sysAppService.save(sysApp);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:app:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysAppService.removeByIds(ids);
    }
}
