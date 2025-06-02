/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.auth.service;

import com.aizuda.boot.modules.auth.param.LoginParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * 授权 服务
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface IAuthService {

    /**
     * 登录设置 COOKIE
     *
     * @param request    请求
     * @param response   响应
     * @param loginParam 登录参数
     * @return 登录信息 MAP
     */
    Map<String, Object> login(HttpServletRequest request, HttpServletResponse response, LoginParam loginParam);

    /**
     * 通过票据登录系统
     *
     * @param request    请求
     * @param response   响应
     * @param loginParam 登录参数
     * @return 登录信息 MAP
     */
    Map<String, Object> tokenLogin(HttpServletRequest request, HttpServletResponse response, LoginParam loginParam);
}
