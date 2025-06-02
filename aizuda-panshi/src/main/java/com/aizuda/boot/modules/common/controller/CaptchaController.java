/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.common.controller;

import com.aizuda.core.api.ApiController;
import com.baomidou.kisso.annotation.LoginIgnore;
import com.baomidou.kisso.annotation.Permission;
import com.baomidou.kisso.captcha.ICaptcha;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 验证码
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "验证码")
@RestController
@RequestMapping("/v1/captcha")
public class CaptchaController extends ApiController {
    @Resource
    private ICaptcha captcha;

    @GetMapping("/image")
    @Permission(ignore = true)
    @LoginIgnore
    public void image(String ticket) {
        try {
            // 验证码信息存放在缓存中，key = ticket 、 value = 验证码文本内容
            captcha.generate(request, response.getOutputStream(), ticket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/verification")
    @Permission(ignore = true)
    @LoginIgnore
    public boolean verification(String ticket, String code) {
        // ticket 为生成验证码的票据， code 为图片验证码文本内容
        return captcha.verification(request, ticket, code);
    }
}
