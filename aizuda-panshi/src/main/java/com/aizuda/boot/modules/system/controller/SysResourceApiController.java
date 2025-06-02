/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysResourceApi;
import com.aizuda.boot.modules.system.service.ISysResourceApiService;
import com.aizuda.core.api.ApiController;
import com.aizuda.core.validation.Create;
import com.aizuda.core.validation.Update;
import com.baomidou.kisso.annotation.Permission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统资源API 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "系统资源API")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/resource-api")
public class SysResourceApiController extends ApiController {
    private ISysResourceApiService sysResourceApiService;

    @Operation(summary = "查询 id 信息")
    @Permission("sys:resourceApi:get")
    @GetMapping("/get")
    public SysResourceApi get(@RequestParam Long id) {
        return sysResourceApiService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:resourceApi:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysResourceApi sra) {
        return sysResourceApiService.updateById(sra);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:resourceApi:create")
    @PostMapping("/create")
    public Long create(@Validated(Create.class) @RequestBody SysResourceApi sra) {
        return sysResourceApiService.saveReturnId(sra);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:resourceApi:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysResourceApiService.removeByResourceIds(ids);
    }
}
