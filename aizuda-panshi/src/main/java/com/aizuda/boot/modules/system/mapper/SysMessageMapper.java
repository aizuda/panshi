package com.aizuda.boot.modules.system.mapper;

import com.aizuda.boot.modules.system.entity.SysMessage;
import com.aizuda.boot.modules.system.entity.vo.MyMessageVO;
import com.aizuda.boot.modules.system.entity.vo.SysMessageVO;
import com.aizuda.service.mapper.CrudMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统消息表 Mapper 接口
 * </p>
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface SysMessageMapper extends CrudMapper<SysMessage> {

    Page<SysMessageVO> selectPageVOByUserId(Page<SysMessage> page, @Param("userId") Long userId, @Param("m") SysMessageVO vo);

    Page<MyMessageVO> selectPageMyNotViewed(Page<MyMessageVO> page, @Param("userId") Long userId, @Param("category") Integer category);
}