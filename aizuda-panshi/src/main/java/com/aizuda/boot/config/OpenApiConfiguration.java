package com.aizuda.boot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApi 配置信息
 *
 * @author 青苗
 * @since 1.0.0
 */
@OpenAPIDefinition
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI springShopOpenAPI() {
        final String loginToken = "accessToken";
        return new OpenAPI().info(new Info().title("AiZuDa PanShi API")
                        .description("爱组搭 PanShi 磐石 ~ 每个人都是架构师，组件搭配干活不累！")
                        .version("v1.0.0")).externalDocs(new ExternalDocumentation()
                        .description("爱组搭低代码组件化开发平台")
                        .url("https://aizuda.com"))
                .components(new Components().addSecuritySchemes(loginToken, new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name(loginToken)))
                .addSecurityItem(new SecurityRequirement().addList(loginToken));
    }

    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder().group("v1").pathsToMatch("/v1/**").build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder().group("系统授权").pathsToMatch("/auth/**").build();
    }

    @Bean
    public GroupedOpenApi sysApi() {
        return GroupedOpenApi.builder().group("系统管理").pathsToMatch("/sys/**").build();
    }

    @Bean
    public GroupedOpenApi genApi() {
        return GroupedOpenApi.builder().group("代码生成").pathsToMatch("/gen/**").build();
    }
}

