package com.aizuda.boot.modules.system.entity;

import com.aizuda.core.bean.SuperEntity;
import com.aizuda.core.validation.Create;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 消息接收人表
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysMessageReceiver", description = "消息接收人表")
@TableName("sys_message_receiver")
public class SysMessageReceiver extends SuperEntity {

	@Schema(description = "消息ID")
	@NotNull(groups = Create.class)
	@PositiveOrZero
	private Long messageId;

	@Schema(description = "接收人ID")
	@NotNull(groups = Create.class)
	@PositiveOrZero
	private Long userId;

	@Schema(description = "已查看 0，否 1，是")
	@PositiveOrZero
	private Integer viewed;

	@Schema(description = "发送状态 0，未发送 1，成功 2，失败")
	@PositiveOrZero
	private Integer sendStatus;

	@Schema(description = "发送失败原因")
	@Size(max = 255)
	private String sendFailure;

	@Schema(description = "发送时间")
	private Date sendTime;

}
