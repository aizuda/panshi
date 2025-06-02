package com.aizuda.boot.modules.gen.controller;

import com.aizuda.boot.modules.gen.entity.dto.GenDTO;
import com.aizuda.boot.modules.gen.entity.vo.GenVO;
import com.aizuda.boot.modules.gen.service.IGenTableService;
import com.aizuda.core.api.ApiController;
import com.baomidou.kisso.annotation.Permission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 代码生成业务表 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "代码生成业务表")
@RestController
@AllArgsConstructor
@RequestMapping("/gen/table")
public class GenTableController extends ApiController {
    private IGenTableService genTableService;

    @Operation(summary = "预览")
    @Permission("gen:table:preview")
    @PostMapping("/preview")
    public List<GenVO> preview(@Validated @RequestBody GenDTO dto) {
        return genTableService.preview(dto);
    }

    @Operation(summary = "下载")
    @Permission("gen:table:download")
    @PostMapping("/download")
    public void download(@Validated @RequestBody GenDTO dto) {
        genTableService.download(response, dto);
    }

}
