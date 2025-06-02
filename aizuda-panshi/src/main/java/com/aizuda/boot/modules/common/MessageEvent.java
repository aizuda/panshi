package com.aizuda.boot.modules.common;

import com.aizuda.core.bean.BeanConvert;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 消息事件对象
 */
@Getter
@Setter
public class MessageEvent implements BeanConvert, Serializable {
    /**
     * 创建人ID
     */
    protected Long createId;

    /**
     * 创建人
     */
    protected String createBy;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 类别 0，通知 1，消息 2，待办
     */
    private Integer category;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 接收人ID列表
     */
    private List<Long> userIds;

}
