/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.service;

import com.aizuda.service.mapper.CrudMapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 自定义 IBaseService 实现
 *
 * @author 青苗
 * @since 1.1.0
 */
public class BaseServiceImpl<M extends CrudMapper<T>, T> extends CrudRepository<M, T> implements IBaseService<T> {

}
