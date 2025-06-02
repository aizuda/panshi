/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.auth.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 登录参数
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "LoginParam", description = "登录参数")
public class LoginParam implements Serializable {

    @Schema(description = "AES登录票据")
    private String token;

    @Schema(description = "登录名称")
    private String username;

    @Schema(description = "登录密码")
    private String password;

    @Schema(description = "验证码")
    private String code;

}
