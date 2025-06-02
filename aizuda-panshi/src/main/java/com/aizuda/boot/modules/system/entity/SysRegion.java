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
 * 行政区域
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysRegion", description = "行政区域")
public class SysRegion extends BaseEntity {

	@Schema(description = "父ID")
	@NotNull(groups = Create.class)
	@PositiveOrZero
	private Long pid;

	@Schema(description = "名称")
	@NotBlank(groups = Create.class)
	@Size(max = 30)
	@TableField(condition = SqlCondition.LIKE)
	private String name;

	@Schema(description = "编码")
	@NotBlank(groups = Create.class)
	@Size(max = 30)
	@TableField(condition = SqlCondition.LIKE)
	private String code;

	@Schema(description = "排序")
	@PositiveOrZero
	@OrderBy
	private Integer sort;

	@Schema(description = "级别 0、省份直辖市 1、地市 2、区县")
	@PositiveOrZero
	private Integer level;

}
