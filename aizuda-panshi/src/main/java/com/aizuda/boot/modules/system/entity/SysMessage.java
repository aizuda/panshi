package com.aizuda.boot.modules.system.entity;

import com.aizuda.core.ApiConstants;
import com.aizuda.core.bean.SuperEntity;
import com.aizuda.core.validation.Create;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 系统消息表
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysMessage", description = "系统消息表")
@TableName("sys_message")
public class SysMessage  extends SuperEntity {

	@Schema(description = "创建人ID")
	@TableField(fill = FieldFill.INSERT)
	protected Long createId;

	@Schema(description = "创建人")
	@TableField(fill = FieldFill.INSERT, condition = SqlCondition.LIKE)
	protected String createBy;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = ApiConstants.DATE_MM)
	@TableField(fill = FieldFill.INSERT)
	protected Date createTime;

	@Schema(description = "修改人")
	@TableField(fill = FieldFill.UPDATE)
	protected String updateBy;

	@Schema(description = "修改时间")
	@JsonFormat(pattern = ApiConstants.DATE_MM)
	@TableField(fill = FieldFill.UPDATE)
	protected Date updateTime;

	@Schema(description = "标题")
	@Size(max = 255)
	@TableField(condition = SqlCondition.LIKE)
	private String title;

	@Schema(description = "内容")
	@Size(max = 800)
	private String content;

	@Schema(description = "类别 0，通知 1，消息 2，待办")
	@NotNull(groups = Create.class)
	@PositiveOrZero
	private Integer category;

	@Schema(description = "业务ID")
	@PositiveOrZero
	private Long businessId;

	@Schema(description = "业务类型")
	@Size(max = 50)
	private String businessType;

}
