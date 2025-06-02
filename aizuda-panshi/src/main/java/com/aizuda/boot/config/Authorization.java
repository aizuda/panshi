package com.aizuda.boot.config;

import com.aizuda.boot.modules.system.service.ISysResourceApiService;
import com.aizuda.service.web.UserSession;
import com.baomidou.kisso.SSOAuthorization;
import com.baomidou.kisso.security.token.SSOToken;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 权限授权处理器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Component
public class Authorization implements SSOAuthorization {
    @Resource
    private ISysResourceApiService sysResourceApiService;

    @Override
    public boolean isPermitted(SSOToken token, String permission) {
        // 暂不验证权限
        return true;
//        Long userId = Long.valueOf(token.getId());
//        if (UserSession.isAdmin(userId)) {
//            // 超级管理员免鉴权
//            return true;
//        }
//        return sysResourceApiService.isPermitted(userId, permission);
    }
}
