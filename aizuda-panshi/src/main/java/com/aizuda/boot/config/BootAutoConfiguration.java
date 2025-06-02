/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.config;

import com.baomidou.kisso.captcha.ImageCaptcha;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.aizuda.boot.modules.*.mapper")
public class BootAutoConfiguration {

    /**
     * 注入图片验证码
     */
    @Bean
    public ImageCaptcha imageCaptcha() {
        ImageCaptcha imageCaptcha = ImageCaptcha.getInstance();
        // 干扰量 1
        imageCaptcha.setInterfere(1);
        // 验证码内容长度 4 位
        imageCaptcha.setLength(4);
        return imageCaptcha;
    }
}
