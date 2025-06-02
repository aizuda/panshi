/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.service.ISysSSEService;
import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.service.web.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * SSE消息推送 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysSSEServiceImpl implements ISysSSEService {
    private final static Map<Long, ConcurrentHashMap<String, SseEmitter>> USER_TOKEN_EMITTERS = new ConcurrentHashMap<>();

    @Override
    public SseEmitter connect() {
        UserSession userSession = UserSession.getLoginInfo();
        Map<String, SseEmitter> emitters = USER_TOKEN_EMITTERS.computeIfAbsent(userSession.getId(), k -> new ConcurrentHashMap<>());

        // 创建一个新的 SseEmitter 实例，默认30秒超时，设置为0L则永不超时
        SseEmitter emitter = new SseEmitter(0L);

        // 用户会话ID
        String sid = userSession.getSid();
        if (null != sid) {
            try {
                // 会话关联推送
                emitters.put(sid, emitter);

                // 当 emitter 完成、超时或发生错误时，从映射表中移除对应的会话信息
                emitter.onCompletion(() -> emitters.remove(sid));
                emitter.onTimeout(() -> emitters.remove(sid));
                emitter.onError((e) -> emitters.remove(sid));

                // 向客户端发送一条连接成功的事件
                emitter.send(SseEmitter.event().comment("connected"));
            } catch (IOException e) {
                // 如果发送消息失败，则从映射表中移除 emitter
                emitters.remove(sid);
            }
        }
        return emitter;
    }

    @Override
    public boolean send(Long userId, String eventName, String message) {
        boolean result = false;
        Map<String, SseEmitter> emitters = USER_TOKEN_EMITTERS.get(userId);
        if (emitters != null) {
            for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
                SseEmitter sseEmitter = entry.getValue();
                try {
                    sseEmitter.send(SseEmitter.event().name(eventName).data(message, APPLICATION_JSON));
                    if (!result) {
                        // 只要推送一次成功认为成功
                        result = true;
                    }
                } catch (Exception e) {
                    emitters.remove(entry.getKey());
                    sseEmitter.completeWithError(e);
                }
            }
        }
        return result;
    }

    @Override
    public boolean sendRemind(Long userId, String title, String content) {
        return this.send(userId, "remind", this.toMessage(title, content));
    }

    /**
     * 组装消息
     *
     * @param title   消息标题
     * @param content 消息内容
     * @return 实际发送消息内容
     */
    private String toMessage(String title, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("content", content);
        return JacksonUtils.toJson(map);
    }
}
