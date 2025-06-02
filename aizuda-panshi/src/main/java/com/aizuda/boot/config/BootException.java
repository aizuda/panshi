/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * 自定义异常统一提示
 */
@Slf4j
@RestControllerAdvice
public class BootException {

    /**
     * IO 异常处理
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void handleIOException(IOException e, HttpServletRequest request) {
        // 捕获 IO 异常，例如 sse 连接断开
        String uri = request.getRequestURI();
        if (!uri.contains("/sse")) {
            // 记录非 sse 请求的异常日志
            log.error("请求地址 {} 连接中断 {}", uri, e.getMessage());
        }
    }
}
