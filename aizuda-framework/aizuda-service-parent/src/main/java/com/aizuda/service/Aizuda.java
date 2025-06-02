/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service;

import com.aizuda.common.toolkit.ThrowableUtils;
import com.aizuda.service.spring.SpringHelper;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 *
 * @author 青苗
 * @since 1.1.0
 */
public class Aizuda {

    /**
     * Spring Boot 启动
     *
     * @param args           运行参数
     * @param primarySources 启动类
     */
    public static void startup(String[] args, Class<?>... primarySources) {
        try {
            SpringApplication application = new SpringApplication(primarySources);
            application.setBannerMode(Banner.Mode.CONSOLE);
            SpringHelper.setApplicationContext(application.run(args));
        } catch (Throwable t) {
            ThrowableUtils.getStackTrace(t);
        }
    }
}
