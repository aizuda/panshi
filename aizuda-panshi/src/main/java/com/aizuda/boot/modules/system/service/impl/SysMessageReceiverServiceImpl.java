package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysMessageReceiver;
import com.aizuda.boot.modules.system.mapper.SysMessageReceiverMapper;
import com.aizuda.boot.modules.system.service.ISysMessageReceiverService;
import com.aizuda.service.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 消息接收人表 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysMessageReceiverServiceImpl extends BaseServiceImpl<SysMessageReceiverMapper, SysMessageReceiver> implements ISysMessageReceiverService {

    @Override
    public boolean updateViewed(Long messageId, Long userId) {
        lambdaUpdate().set(SysMessageReceiver::getViewed, 1)
                .eq(null != messageId, SysMessageReceiver::getMessageId, messageId)
                .eq(SysMessageReceiver::getUserId, userId)
                .update();
        return true;
    }
}
