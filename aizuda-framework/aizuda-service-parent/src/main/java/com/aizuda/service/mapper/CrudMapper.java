/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 自定义批量插入 Mapper 继承即可获得批量插入能力
 *
 * @param <T> 实体对象泛型
 * @author 青苗
 * @since 1.1.0
 */
public interface CrudMapper<T> extends BaseMapper<T> {

    Integer insertBatch(@Param("list") Collection<T> t);
}
