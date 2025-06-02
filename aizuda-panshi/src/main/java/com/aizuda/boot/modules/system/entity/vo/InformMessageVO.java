package com.aizuda.boot.modules.system.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InformMessageVO {

    @Schema(description = "总数量")
    private Long totalNum;

    @Schema(description = "通知总数量")
    private Long noticeNum;

    @Schema(description = "通知")
    private List<MyMessageVO> noticeList;

    @Schema(description = "消息总数量")
    private Long messageNum;

    @Schema(description = "消息")
    private List<MyMessageVO> messageList;

    @Schema(description = "待办总数量")
    private Long todoNum;

    @Schema(description = "待办")
    private List<MyMessageVO> todoList;

    public Long getNoticeNum() {
        return null == noticeNum ? 0 : noticeNum;
    }

    public Long getMessageNum() {
        return null == messageNum ? 0 : messageNum;
    }

    public Long getTodoNum() {
        return null == todoNum ? 0 : todoNum;
    }
}
