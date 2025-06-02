package com.aizuda.boot.modules.common.controller;

import com.aizuda.core.api.ApiController;
import com.baomidou.kisso.annotation.LoginIgnore;
import com.baomidou.kisso.annotation.Permission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

// 阅读文档  http://aizuda.com/article/1086603
@Tag(name = "多语言")
@Slf4j
@RestController
@RequestMapping("/v1/i18n")
@AllArgsConstructor
public class I18nController extends ApiController {
    private MessageSource messageSource;

    // 切换英语 http://localhost:8088/v1/i18n/language/us
    // 切换中文 http://localhost:8088/v1/i18n/language/cn
    @LoginIgnore
    @Operation(summary = "多语言切换")
    @GetMapping("/language/{lang}")
    public Boolean language(@PathVariable("lang") String lang) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        localeResolver.setLocale(request, response, "us".equals(lang) ? Locale.US : Locale.CHINA);
        return true;
    }

    /**
     * i18n 多语言测试 http://localhost:8088/v1/i18n/test
     * <p>
     * 切换英文 http://localhost:8088/v1/i18n/language?locale=en_US
     * </p>
     * <p>
     * 切换中文 http://localhost:8088/v1/i18n/test?locale=zh_CN
     * </p>
     */
    @LoginIgnore
    @Permission(ignore = true)
    @GetMapping("/test")
    public String test() {
        try {
            // 异常多语言处理 ApiAssert.fail("test.i18n", messageSource);
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage("test.i18n", null, locale);
        } catch (NoSuchMessageException e) {
            // Do Nothing
        }
        return "error";
    }
}
