package com.aizuda.boot.modules.system.entity.vo;

import com.aizuda.boot.modules.system.entity.SysMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysMessageVO extends SysMessage {

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
