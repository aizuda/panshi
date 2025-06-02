package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysMessageReceiver;
import com.aizuda.service.service.IBaseService;

/**
 * 消息接收人表 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysMessageReceiverService extends IBaseService<SysMessageReceiver> {

    /**
     * 更新已读
     *
     * @param messageId 消息ID
     * @param userId    用户ID
     */
    boolean updateViewed(Long messageId, Long userId);
}
