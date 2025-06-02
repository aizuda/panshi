/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.web;

import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.spring.SpringHelper;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.security.token.SSOToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

/**
 * <a href="http://aizuda.com">爱组搭</a>
 * ----------------------------------------
 * 用户会话信息
 *
 * @author 青苗
 * @since 1.1.0
 */
@Getter
@Setter
public class UserSession {
    // 会话ID
    private String sid;
    // 用户ID
    private String userId;
    // 用户名
    private String username;

    public UserSession(String id, String issuer) {
        this.userId = id;
        this.username = issuer;
    }

    public Long getId() {
        return Long.valueOf(this.userId);
    }

    public static UserSession getLoginInfo() {
        return getLoginInfo(false);
    }

    public static UserSession getLoginInfo(boolean allowNull) {
        return getLoginInfo(SpringHelper.getCurrentRequest(), allowNull);
    }

    public static UserSession getLoginInfo(HttpServletRequest request, boolean allowNull) {
        SSOToken ssoToken = getSSOToken(request);
        if (null == ssoToken) {
            if (allowNull) {
                return null;
            }
            ApiAssert.fail("未登录");
        }
        UserSession userSession = new UserSession(ssoToken.getId(), ssoToken.getIssuer());
        Map<String, Object> data = ssoToken.getData();
        if (null != data) {
            userSession.setSid((String) data.get("sid"));
        }
        return userSession;
    }

    /**
     * 获取当前登录账户 SSOToken 登录票据
     *
     * @param request 当前请求
     * @return SSOToken 登录票据
     */
    private static SSOToken getSSOToken(HttpServletRequest request) {
        SSOToken ssoToken = null;
        if (null != request) {
            ssoToken = SSOHelper.attrToken(request);
            if (null == ssoToken) {
                ssoToken = SSOHelper.getSSOToken(request);
            }
        }
        return ssoToken;
    }

    /**
     * 判断是否为管理员
     *
     * @param id 用户ID
     */
    public static boolean isAdmin(Long id) {
        return Objects.equals(0L, id);
    }
}
