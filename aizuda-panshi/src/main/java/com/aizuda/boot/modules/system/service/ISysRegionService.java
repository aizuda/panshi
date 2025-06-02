/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import com.aizuda.boot.modules.system.entity.SysRegion;
import com.aizuda.boot.modules.system.entity.vo.SysRegionVO;
import com.aizuda.service.service.IBaseService;

import java.util.List;

/**
 * 行政区域 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysRegionService extends IBaseService<SysRegion> {

    List<SysRegionVO> listTree(SysRegion sysRegion);
}
