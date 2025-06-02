package com.aizuda.boot.modules.gen.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenDTO {

    @Schema(description = "数据源ID，默认当前数据源")
    private Long databaseId;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "项目模块")
    @NotBlank
    private String module;

    @Schema(description = "表名，多个英文逗号分隔")
    @NotBlank
    private String tableName;

    @Schema(description = "模板ID列表")
    @NotEmpty
    private List<Long> templateIds;

}
