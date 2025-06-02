/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity;

import com.aizuda.core.ApiConstants;
import com.aizuda.core.bean.BaseEntity;
import com.aizuda.core.validation.Create;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 应用
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysApp", description = "应用")
public class SysApp extends BaseEntity {

	@Schema(description = "标识")
	@NotBlank(groups = Create.class)
	@Size(max = 100)
	@TableField(condition = SqlCondition.LIKE)
	private String identification;

	@Schema(description = "名称")
	@NotBlank(groups = Create.class)
	@Size(max = 100)
	@TableField(condition = SqlCondition.LIKE)
	private String name;

	@Schema(description = "密钥")
	@NotBlank(groups = Create.class)
	@Size(max = 255)
	private String secretKey;

	@Schema(description = "状态 0、禁用 1、正常")
	private Integer status;

	@Schema(description = "授权到期")
	@NotNull(groups = Create.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ApiConstants.DATE_MM_SS, timezone = ApiConstants.GMT8)
	private Date expire;

	@Schema(description = "排序")
	@PositiveOrZero
	@OrderBy
	private Integer sort;

}
