/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity;

import com.aizuda.core.bean.BaseEntity;
import com.aizuda.core.validation.Create;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysResource", description = "系统资源")
public class SysResource extends BaseEntity {

	@Schema(description = "父ID")
	@NotNull(groups = Create.class)
	@PositiveOrZero
	private Long pid;

	@Schema(description = "名称")
	@NotBlank(groups = Create.class)
	@Size(max = 60)
	private String title;

	@Schema(description = "别名")
	@Size(max = 60)
	private String alias;

	@Schema(description = "类型 0，菜单 1，iframe 2，外链 3，按钮")
	@NotNull(groups = Create.class)
	@PositiveOrZero
	private Integer type;

	@Schema(description = "编码")
	@Size(max = 100)
	private String code;

	@Schema(description = "重定向")
	@Size(max = 100)
	private String redirect;

	@Schema(description = "文件路径")
	@Size(max = 100)
	private String path;

	@Schema(description = "图标")
	@Size(max = 30)
	private String icon;

	@Schema(description = "状态 0、禁用 1、正常")
	@PositiveOrZero
	private Integer status;

	@Schema(description = "排序")
	@PositiveOrZero
	private Integer sort;

	@Schema(description = "视图")
	@Size(max = 255)
	private String component;

	@Schema(description = "颜色")
	@Size(max = 30)
	private String color;

	@Schema(description = "隐藏菜单")
	private Boolean hidden;

	@Schema(description = "上级路由")
	private String parentRoute;

	@Schema(description = "保留查询参数")
	private Boolean keepAlive;

	@Schema(description = "查询携带参数")
	private String query;

}
