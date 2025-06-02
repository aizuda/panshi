/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysRegion;
import com.aizuda.boot.modules.system.entity.vo.SysRegionVO;
import com.aizuda.boot.modules.system.service.ISysRegionService;
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
 * 行政区域 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "行政区域")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/region")
public class SysRegionController extends ApiController {
    private ISysRegionService sysRegionService;

    @Operation(summary = "树列表")
    @Permission("sys:region:listTree")
    @PostMapping("/list-tree")
    public List<SysRegionVO> listTree(@RequestBody SysRegion sysRegion) {
        return sysRegionService.listTree(sysRegion);
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:region:get")
    @GetMapping("/get")
    public SysRegion get(@RequestParam Long id) {
        return sysRegionService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:region:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysRegion sysRegion) {
        return sysRegionService.updateById(sysRegion);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:region:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysRegion sysRegion) {
        return sysRegionService.save(sysRegion);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:region:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysRegionService.removeByIds(ids);
    }
}
