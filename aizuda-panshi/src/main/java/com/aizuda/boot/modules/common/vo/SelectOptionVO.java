package com.aizuda.boot.modules.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 下拉选择项对象
 */
@Getter
@Setter
public class SelectOptionVO implements Serializable {

    @Schema(description = "主键 ID")
    protected Long id;

    @Schema(description = "显示标题")
    private String title;

    @Schema(description = "选择内容")
    private Object content;

    public static SelectOptionVO of(String title, Object content) {
        SelectOptionVO vo = new SelectOptionVO();
        vo.setTitle(title);
        vo.setContent(content);
        return vo;
    }

    public static SelectOptionVO of(Long id, String title) {
        SelectOptionVO vo = new SelectOptionVO();
        vo.setId(id);
        vo.setTitle(title);
        return vo;
    }
}
