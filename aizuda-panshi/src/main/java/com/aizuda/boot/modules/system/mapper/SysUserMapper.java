/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.mapper;

import com.aizuda.boot.modules.system.entity.SysUser;
import com.aizuda.boot.modules.system.entity.vo.SysUserVO;
import com.aizuda.service.mapper.CrudMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface SysUserMapper extends CrudMapper<SysUser> {

    Page<SysUser> selectPageByVO(Page page, @Param("vo") SysUserVO vo);

}
