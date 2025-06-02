/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.service;

import com.aizuda.core.api.ApiAssert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.function.Supplier;

/**
 *  <a href="http://aizuda.com">爱组搭</a>
 * ----------------------------------------
 * 自定义 Service 基类
 *
 * @author 青苗
 * @since 1.1.0
 */
public interface IBaseService<T> extends IRepository<T> {

    /**
     * 校验指定条件是否存在
     *
     * @param condition 判断条件
     * @param supplier  查询条件
     * @param message   存在提示
     */
    default void checkExists(boolean condition, Supplier<LambdaQueryWrapper<T>> supplier, String message) {
        if (condition) {
            checkExists(supplier.get(), message);
        }
    }

    /**
     * 校验指定条件是否存在
     *
     * @param lqw     查询条件 LambdaQueryWrapper
     * @param message 存在提示
     */
    default void checkExists(LambdaQueryWrapper<T> lqw, String message) {
        ApiAssert.fail(count(lqw) > 0, message);
    }

    /**
     * 根据 ID 查询 检查数据合法性
     *
     * @param id 主键ID
     */
    default T checkById(Long id) {
        T t = this.getById(id);
        ApiAssert.fail(null == t, "指定ID查询数据不存在");
        return t;
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     */
    default boolean saveBatch(Collection<T> entityList) {
        return saveBatch(entityList, 100);
    }
}
