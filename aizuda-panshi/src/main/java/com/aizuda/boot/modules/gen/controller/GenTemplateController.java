package com.aizuda.boot.modules.gen.controller;

import com.aizuda.boot.modules.gen.entity.GenTemplate;
import com.aizuda.boot.modules.gen.service.IGenTemplateService;
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
 * 代码生成模板表 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "代码生成模板表")
@RestController
@AllArgsConstructor
@RequestMapping("/gen/template")
public class GenTemplateController extends ApiController {
    private IGenTemplateService genTemplateService;

    @Operation(summary = "分页列表")
    @Permission("gen:template:page")
    @PostMapping("/page")
    public Page<GenTemplate> getPage(@RequestBody PageParam<GenTemplate> dto) {
        return genTemplateService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "所有列表")
    @Permission("gen:template:listAll")
    @PostMapping("/list-all")
    public List<GenTemplate> getPage() {
        return genTemplateService.listAll();
    }

    @Operation(summary = "查询 id 信息")
    @Permission("gen:template:get")
    @GetMapping("/get")
    public GenTemplate get(@RequestParam Long id) {
        return genTemplateService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("gen:template:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody GenTemplate genTemplate) {
        return genTemplateService.updateById(genTemplate);
    }

    @Operation(summary = "创建添加")
    @Permission("gen:template:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody GenTemplate genTemplate) {
        return genTemplateService.save(genTemplate);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("gen:template:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return genTemplateService.removeByIds(ids);
    }
}
