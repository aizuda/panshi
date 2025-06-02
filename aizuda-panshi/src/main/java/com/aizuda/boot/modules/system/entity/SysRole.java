/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity;

import com.aizuda.core.bean.BaseEntity;
import com.aizuda.core.validation.Create;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统角色
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysRole", description = "系统角色")
public class SysRole extends BaseEntity {

    @Schema(description = "名称")
    @NotBlank(groups = Create.class)
    @Size(max = 30)
    @TableField(condition = SqlCondition.LIKE)
    private String name;

    @Schema(description = "别名")
    @NotBlank(groups = Create.class)
    @Size(max = 30)
    @TableField(condition = SqlCondition.LIKE)
    private String alias;

    @Schema(description = "备注")
    @Size(max = 255)
    @TableField(condition = SqlCondition.LIKE)
    private String remark;

    @Schema(description = "状态 0、禁用 1、正常")
    private Integer status;

    @OrderBy
    @Schema(description = "排序")
    @NotNull(groups = Create.class)
    @PositiveOrZero
    private Integer sort;

}
