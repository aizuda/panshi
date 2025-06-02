/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE消息推送 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface ISysSSEService {

    /**
     * SSE 连接
     */
    SseEmitter connect();

    /**
     * 指定用户ID SSE 推送消息
     *
     * @param userId    用户ID
     * @param eventName 事件名称
     * @param message   消息内容
     */
    boolean send(Long userId, String eventName, String message);

    /**
     * 指定用户ID SSE 推送提醒消息
     *
     * @param userId  用户ID
     * @param title   消息标题
     * @param content 消息内容
     */
    boolean sendRemind(Long userId, String title, String content);
}
