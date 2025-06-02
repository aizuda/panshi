/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.auth.controller;

import com.aizuda.boot.modules.auth.param.LoginParam;
import com.aizuda.boot.modules.auth.service.IAuthService;
import com.aizuda.core.api.ApiController;
import com.baomidou.kisso.annotation.LoginIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 登录 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "授权")
@RestController
@AllArgsConstructor
@RequestMapping("/auth/login")
public class LoginController extends ApiController {
    private IAuthService authService;

    @Operation(summary = "登录系统")
    @LoginIgnore
    @PostMapping("/system")
    public Map<String, Object> loginSystem(@RequestBody LoginParam loginParam) {
        return authService.login(request, response, loginParam);
    }

    @Operation(summary = "通过票据登录")
    @LoginIgnore
    @PostMapping("/token")
    public Map<String, Object> tokenLogin(@RequestBody LoginParam loginParam) {
        return authService.tokenLogin(request, response, loginParam);
    }
}
