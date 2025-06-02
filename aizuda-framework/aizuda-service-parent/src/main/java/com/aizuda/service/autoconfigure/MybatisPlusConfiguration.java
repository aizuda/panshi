/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.autoconfigure;

import com.aizuda.common.toolkit.CollectionUtils;
import com.aizuda.service.interceptor.PerformanceInterceptor;
import com.aizuda.service.mapper.AizudaSqlInjector;
import com.aizuda.service.web.AizudaMetaObjectHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * MybatisPlus配置
 *
 * @author 青苗
 * @since 1.1.0
 */
@Lazy
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
public class MybatisPlusConfiguration {

    /**
     * mybatis-plus 插件<br>
     * 文档：http://baomidou.com
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Autowired(required = false) List<InnerInterceptor> innerInterceptorList) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (CollectionUtils.isNotEmpty(innerInterceptorList)) {
            // 注入自定义插件
            innerInterceptorList.forEach(i -> interceptor.addInnerInterceptor(i));
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public AizudaSqlInjector batchSqlInjector() {
        return new AizudaSqlInjector();
    }

    @Bean
    @ConditionalOnMissingBean
    public AizudaMetaObjectHandler metaObjectHandler() {
        return new AizudaMetaObjectHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybatis-plus", name = "sql-print", havingValue = "true")
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}
