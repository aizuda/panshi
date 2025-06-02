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
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 扩展配置
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysConfigure", description = "扩展配置")
public class SysConfigure extends BaseEntity {

	@Schema(description = "分类")
	@NotBlank(groups = Create.class)
	@Size(max = 255)
	private String category;

	@Schema(description = "关键字")
	@NotBlank(groups = Create.class)
	@Size(max = 255)
	@TableField(condition = SqlCondition.LIKE)
	private String keyword;

	@Schema(description = "内容")
	@NotBlank(groups = Create.class)
	private String content;

	@Schema(description = "标题")
	@NotBlank(groups = Create.class)
	@Size(max = 255)
	@TableField(condition = SqlCondition.LIKE)
	private String title;

	@Schema(description = "排序")
	@PositiveOrZero
	@OrderBy
	private Integer sort;

}
