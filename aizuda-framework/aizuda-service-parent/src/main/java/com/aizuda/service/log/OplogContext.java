/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.log;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 操作日志上下文
 *
 * @author 青苗
 * @since 1.1.0
 */
public class OplogContext {
    /**
     * 操作日志本地线程
     */
    protected static ThreadLocal<Oplog> OPLOG_HOLDER = new ThreadLocal<>();

    /**
     * 构建操作日志上下文对象
     *
     */
    public static Oplog builder() {
        Oplog oplog = OPLOG_HOLDER.get();
        if (null == oplog) {
            oplog = new Oplog();
        }
        return oplog;
    }

    /**
     * 删除本地线程操作日志
     */
    public static void remove() {
        OPLOG_HOLDER.remove();
    }
}
