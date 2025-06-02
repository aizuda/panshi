/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.autoconfigure;

import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.log.IOplogStorageProvider;
import com.aizuda.service.log.OplogAspect;
import com.aizuda.service.web.IExcludePaths;
import com.aizuda.service.web.ServiceExceptionHandler;
import com.aizuda.service.web.ServiceWebMvcConfigurer;
import com.baomidou.kisso.SSOAuthorization;
import com.baomidou.kisso.web.auth.BasicAuthenticateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/***
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 启动初始化配置
 *
 * @author 青苗
 * @since 1.1.0
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@Import({JsonAutoConfiguration.class, MybatisPlusConfiguration.class})
public class AizudaAutoConfiguration {

    /**
     * 全局异常处理
     */
    @Bean
    @ConditionalOnMissingBean
    public ServiceExceptionHandler serviceExceptionHandler() {
        return new ServiceExceptionHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "kisso.config", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public ServiceWebMvcConfigurer serviceWebMvcConfigurer(@Autowired(required = false) SSOAuthorization ssoAuthorization,
                                                           @Autowired(required = false) IExcludePaths excludePaths) {
        ApiAssert.isEmpty(ssoAuthorization, "SSOAuthorization Implementation class not found");
        return new ServiceWebMvcConfigurer(ssoAuthorization, excludePaths);
    }

    /**
     * spring boot admin 访问监控权限拦截器配置
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.boot.admin.client.instance.metadata", name = "user.name")
    public FilterRegistrationBean basicAuthenticateFilter(@Value("${spring.boot.admin.client.instance.metadata.user.name}") String username,
                                                          @Value("${spring.boot.admin.client.instance.metadata.user.password}") String password) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new BasicAuthenticateFilter(username, password));
        registrationBean.addUrlPatterns("/actuator", "/actuator/**");
        registrationBean.setOrder(Integer.MAX_VALUE);
        return registrationBean;
    }

    /**
     * 操作日志拦截切面
     */
    @Bean
    @ConditionalOnBean(IOplogStorageProvider.class)
    public OplogAspect logRecordAspect(IOplogStorageProvider oplogStorageProvider) {
        return new OplogAspect(oplogStorageProvider);
    }
}
