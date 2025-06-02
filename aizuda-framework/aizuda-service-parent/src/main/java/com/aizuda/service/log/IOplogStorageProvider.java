/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.log;

import org.aspectj.lang.Signature;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 日志存储提供者
 *
 * @author 青苗
 * @since 1.1.0
 */
public interface IOplogStorageProvider {

    /**
     * 保存日志
     *
     * @param signature {@link Signature}
     * @param oplog     操作日志
     */
    void save(Signature signature, Oplog oplog);
}
