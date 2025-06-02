
package com.aizuda.boot.modules.gen.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenVO {

    @Schema(description = "模板名称")
    private String tplName;

    @Schema(description = "模板内容")
    private String tplContent;

    @Schema(description = "输出文件")
    private String outFile;

    @Schema(description = "模板描述")
    private String remark;
}
