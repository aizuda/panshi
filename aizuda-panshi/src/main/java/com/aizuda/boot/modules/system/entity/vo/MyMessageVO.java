package com.aizuda.boot.modules.system.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MyMessageVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "业务ID")
    private Long businessId;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "发布人")
    private String createBy;

    @Schema(description = "发布时间")
    private Date createTime;

}
