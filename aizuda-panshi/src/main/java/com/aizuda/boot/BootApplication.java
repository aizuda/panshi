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
package com.aizuda.boot;

import com.aizuda.service.Aizuda;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <a href="http://aizuda.com">爱组搭</a>
 *
 * @author 青苗
 * @since 1.0.0
 */
@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        // 启动
        Aizuda.startup(args, BootApplication.class);
        System.err.println("http://localhost:8088/swagger-ui/index.html");
    }
}

