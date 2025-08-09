/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.auth.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 刷新票据登录参数
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "RefreshTokenParam", description = "刷新票据登录参数")
public class RefreshTokenParam implements Serializable {

    @NotEmpty
    @Schema(description = "刷新票据")
    private String refreshToken;

}
