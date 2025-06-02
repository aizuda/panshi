package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.service.ISysSSEService;
import com.aizuda.core.api.ApiController;
import com.aizuda.service.web.UserSession;
import com.baomidou.kisso.annotation.Permission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE消息推送 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "SSE")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/sse")
public class SysSSEController extends ApiController {
    private ISysSSEService sseService;

    @Operation(summary = "连接")
    @Permission("sys:sse:connect")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        return sseService.connect();
    }

    @Operation(summary = "测试发送数据给当前用户")
    @Permission("sys:sse:testSend")
    @GetMapping("/test-send")
    public boolean sendTest(String message) {
        sseService.send(UserSession.getLoginInfo().getId(), "message", message);
        return true;
    }
}