package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysMessage;
import com.aizuda.boot.modules.system.entity.vo.InformMessageVO;
import com.aizuda.boot.modules.system.entity.vo.SysMessageVO;
import com.aizuda.service.service.IBaseService;
import com.aizuda.service.web.UserSession;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 系统消息表 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysMessageService extends IBaseService<SysMessage> {

    Page<SysMessage> page(Page<SysMessage> page, SysMessage sysMessage);

    /**
     * 我的消息分页列表
     */
    Page<SysMessageVO> pageMy(Page<SysMessage> page, SysMessageVO vo);

    /**
     * 告知消息
     */
    InformMessageVO getInformByUser();

    /**
     * 一键已读
     */
    boolean read(Long id);

}
