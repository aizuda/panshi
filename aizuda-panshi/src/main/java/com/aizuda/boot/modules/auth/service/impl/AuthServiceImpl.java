/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.auth.service.impl;

import com.aizuda.boot.modules.auth.param.LoginParam;
import com.aizuda.boot.modules.auth.service.IAuthService;
import com.aizuda.boot.modules.system.entity.SysUser;
import com.aizuda.boot.modules.system.service.ISysUserService;
import com.aizuda.core.api.ApiAssert;
import com.baomidou.kisso.common.encrypt.MD5Salt;
import com.baomidou.kisso.enums.TokenOrigin;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.mybatisplus.core.toolkit.AES;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 授权 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private ISysUserService sysUserService;

    @Override
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response, LoginParam loginParam) {
        ApiAssert.fail(StringUtils.isEmpty(loginParam.getUsername())
                || StringUtils.isEmpty(loginParam.getPassword()), "用户名密码不能为空");
        ApiAssert.fail(!Objects.equals("azd666", loginParam.getCode()), "请输入正确的验证码");
        List<SysUser> userList = sysUserService.list(Wrappers.<SysUser>query().eq("username", loginParam.getUsername()));
        ApiAssert.fail(null == userList || userList.size() != 1, "用户不存或异常数据");
        SysUser user = userList.get(0);
        ApiAssert.fail(Objects.equals(0, user.getStatus()), "用户已禁用，请联系管理员");
        ApiAssert.fail(!MD5Salt.isValid(user.getUsername() + user.getSalt()
                , user.getPassword(), loginParam.getPassword()), "登录密码错误");

        // 登录信息
        return loginInfo(request, user);
    }

    /**
     * 设置登录信息
     */
    private Map<String, Object> loginInfo(HttpServletRequest request, SysUser user) {
        Map<String, Object> loginInfo = new HashMap<>(4);
        loginInfo.put("token", new SSOToken().id(user.getId()).issuer(user.getUsername())
                .userAgent(request).origin(TokenOrigin.HTML5).data(new HashMap<>() {{
                    // 设置会话ID，用于区分客户端消息发送
                    put("sid", IdWorker.get32UUID());
                }}).getToken());
        loginInfo.put("userInfo", new HashMap<String, Object>(2) {{
            put("userId", user.getId());
            put("userName", user.getNickName());
        }});
        return loginInfo;
    }

    @Override
    public Map<String, Object> tokenLogin(HttpServletRequest request, HttpServletResponse response, LoginParam loginParam) {
        ApiAssert.fail(StringUtils.isBlank(loginParam.getToken()), "授权票据不能为空");

        try {
            // 校验票据合法性，Token 为 Base64 URL 加密
            byte[] bytes = Base64.getUrlDecoder().decode(loginParam.getToken());
            String text = new String(AES.decrypt(bytes, "AIjOCLBy8D0BlSdC".getBytes(StandardCharsets.UTF_8)));
            long timestamp = Long.parseLong(text.substring(0, 10));
            long currentTime = System.currentTimeMillis() / 1000;
            if ((currentTime - timestamp) > 600) {
                ApiAssert.fail("授权票据已失效");
            }
        } catch (Throwable t) {
            ApiAssert.fail("授权票据验证失败");
        }

        // 固定进入 admin 账号
        SysUser user = new SysUser();
        user.setId(0L);
        user.setUsername("admin");
        user.setNickName("admin");
        return loginInfo(request, user);
    }

}
