package com.aizuda.boot.modules.gen.controller;

import com.aizuda.boot.modules.common.vo.SelectOptionVO;
import com.aizuda.boot.modules.gen.entity.GenDatabase;
import com.aizuda.boot.modules.gen.service.IGenDatabaseService;
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
 * 代码生成数据源表 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "代码生成数据源表")
@RestController
@AllArgsConstructor
@RequestMapping("/gen/database")
public class GenDatabaseController extends ApiController {
    private IGenDatabaseService genDatabaseService;

    @Operation(summary = "分页列表")
    @Permission("gen:database:page")
    @PostMapping("/page")
    public Page<GenDatabase> getPage(@RequestBody PageParam<GenDatabase> dto) {
        return genDatabaseService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "下拉选择项列表")
    @Permission("gen:database:listSelectOptions")
    @GetMapping("/list-select-options")
    public List<SelectOptionVO> listSelectOptions() {
        return genDatabaseService.listSelectOptions();
    }

    @Operation(summary = "查询 id 信息")
    @Permission("gen:database:get")
    @GetMapping("/get")
    public GenDatabase get(@RequestParam Long id) {
        return genDatabaseService.getNoPasswordById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("gen:database:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody GenDatabase genDatabase) {
        return genDatabaseService.updateById(genDatabase);
    }

    @Operation(summary = "创建添加")
    @Permission("gen:database:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody GenDatabase genDatabase) {
        return genDatabaseService.save(genDatabase);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("gen:database:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return genDatabaseService.removeByIds(ids);
    }
}
