/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysPost;
import com.aizuda.boot.modules.system.service.ISysPostService;
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
 * 岗位 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "岗位")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/post")
public class SysPostController extends ApiController {
    private ISysPostService sysPostService;

    @Operation(summary = "分页列表")
    @Permission("sys:post:page")
    @PostMapping("/page")
    public Page<SysPost> getPage(@RequestBody PageParam<SysPost> dto) {
        return sysPostService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:post:get")
    @GetMapping("/get")
    public SysPost get(@RequestParam Long id) {
        return sysPostService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:post:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysPost sysPost) {
        return sysPostService.updateById(sysPost);
    }

    @Operation(summary = "根据 id 修改状态")
    @Permission("sys:post:status")
    @PostMapping("/status/{id}")
    public boolean status(@PathVariable("id") Long id, @RequestParam Integer status) {
        return sysPostService.updateStatusById(id, status);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:post:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysPost sysPost) {
        return sysPostService.save(sysPost);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:post:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysPostService.removeByIds(ids);
    }
}
