package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.common.MessageEvent;
import com.aizuda.boot.modules.system.entity.SysMessage;
import com.aizuda.boot.modules.system.entity.SysMessageReceiver;
import com.aizuda.boot.modules.system.entity.vo.InformMessageVO;
import com.aizuda.boot.modules.system.entity.vo.MyMessageVO;
import com.aizuda.boot.modules.system.entity.vo.SysMessageVO;
import com.aizuda.boot.modules.system.mapper.SysMessageMapper;
import com.aizuda.boot.modules.system.service.ISysMessageReceiverService;
import com.aizuda.boot.modules.system.service.ISysMessageService;
import com.aizuda.boot.modules.system.service.ISysSSEService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.aizuda.service.web.UserSession;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 系统消息表 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class SysMessageServiceImpl extends BaseServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {
    private ISysMessageReceiverService messageReceiverService;
    private ISysSSEService sseService;

    @Override
    public Page<SysMessage> page(Page<SysMessage> page, SysMessage sysMessage) {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(sysMessage);
        lqw.orderByDesc(SysMessage::getCreateTime);
        return super.page(page, lqw);
    }

    @Override
    public Page<SysMessageVO> pageMy(Page<SysMessage> page, SysMessageVO vo) {
        UserSession userSession = UserSession.getLoginInfo();
        return baseMapper.selectPageVOByUserId(page, userSession.getId(), vo);
    }

    @Override
    public boolean updateById(SysMessage sysMessage) {
        ApiAssert.fail(null == sysMessage.getId(), "主键不存在无法更新");
        return super.updateById(sysMessage);
    }

    @Override
    public InformMessageVO getInformByUser() {
        UserSession userSession = UserSession.getLoginInfo();
        InformMessageVO vo = new InformMessageVO();
        // 通知
        Page<MyMessageVO> noticePage = this.pageCategoryByUser(userSession.getId(), 0);
        vo.setNoticeNum(noticePage.getTotal());
        vo.setNoticeList(noticePage.getRecords());
        // 消息
        Page<MyMessageVO> messagePage = this.pageCategoryByUser(userSession.getId(), 1);
        vo.setMessageNum(messagePage.getTotal());
        vo.setMessageList(messagePage.getRecords());
        // 待办
        Page<MyMessageVO> todoPage = this.pageCategoryByUser(userSession.getId(), 2);
        vo.setTodoNum(todoPage.getTotal());
        vo.setTodoList(todoPage.getRecords());
        // 总数量
        vo.setTotalNum(vo.getNoticeNum() + vo.getMessageNum() + vo.getTodoNum());
        return vo;
    }

    public Page<MyMessageVO> pageCategoryByUser(Long userId, Integer category) {
        return baseMapper.selectPageMyNotViewed(Page.of(1, 5), userId, category);
    }

    @Override
    public boolean read(Long id) {
        UserSession userSession = UserSession.getLoginInfo();
        return messageReceiverService.updateViewed(id, userSession.getId());
    }

    /**
     * 消息处理监听器
     *
     * @param event 消息事件
     */
    @EventListener
    public void onMessageEvent(MessageEvent event) {
        // 保存消息
        SysMessage message = event.convert(SysMessage.class);
        message.setCreateTime(new Date());
        if (super.save(message)) {
            // 保存消息接收者列表
            messageReceiverService.saveBatch(event.getUserIds().stream().map(userId -> {
                SysMessageReceiver receiver = new SysMessageReceiver();
                receiver.setMessageId(message.getId());
                receiver.setUserId(userId);
                // 发送消息
                if (sseService.sendRemind(userId, event.getTitle(), event.getContent())) {
                    receiver.setSendStatus(1);
                } else {
                    receiver.setSendStatus(2);
                    receiver.setSendFailure("未上线");
                }
                receiver.setSendTime(new Date());
                return receiver;
            }).toList());
        }
    }
}
