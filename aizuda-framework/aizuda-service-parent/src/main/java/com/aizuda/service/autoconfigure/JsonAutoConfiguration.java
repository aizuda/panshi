/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.autoconfigure;

import com.aizuda.service.editors.DoubleEditor;
import com.aizuda.service.editors.IntegerEditor;
import com.aizuda.service.editors.LongEditor;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.http.converter.autoconfigure.ServerHttpMessageConvertersCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 爱组搭 <a href="http://aizuda.com">http://aizuda.com</a>
 * ----------------------------------------
 * Json 处理相关配置
 *
 * @author 青苗
 * @since 1.1.0
 */
@Lazy
@RestControllerAdvice
@Configuration(proxyBeanMethods = false)
public class JsonAutoConfiguration {
    @Value("${spring.jackson.time-zone}")
    private String timeZone = "UTC+8";

    /**
     * 字符串转换处理
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 日期转换
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new DateFormat() {
            private final List<? extends DateFormat> DATE_FORMATS = Arrays.asList(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm"),
                    new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy-MM"));

            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
                throw new UnsupportedOperationException("This custom date formatter can only be used to *parse* Dates.");
            }

            @Override
            public Date parse(String source, ParsePosition pos) {
                for (final DateFormat dateFormat : DATE_FORMATS) {
                    Date date = dateFormat.parse(source, pos);
                    if (null != date) {
                        return date;
                    }
                }
                return null;
            }
        }, true));
        // 其他类型转换
        binder.registerCustomEditor(Long.class, new LongEditor());
        binder.registerCustomEditor(Double.class, new DoubleEditor());
        binder.registerCustomEditor(Integer.class, new IntegerEditor());
    }

    /**
     * 消息转换
     */
    @Bean
    public ServerHttpMessageConvertersCustomizer jacksonHttpMessageConverters() {
        SimpleModule simpleModule = new SimpleModule();
        // Long 转为 String 防止 js 丢失精度
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        JsonMapper objectMapper = JsonMapper.builder()
                .changeDefaultPropertyInclusion(value -> JsonInclude.Value.construct(
                        JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
                // 忽略 transient 关键词属性
                .enable(MapperFeature.PROPAGATE_TRANSIENT_MARKER)
                .addModule(simpleModule)
                // 设置时区
                .defaultTimeZone(TimeZone.getTimeZone(timeZone))
                .build();
        HttpMessageConverter<?> jacksonConverter = new JacksonJsonHttpMessageConverter(objectMapper);
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setDefaultCharset(StandardCharsets.UTF_8);
        return serverBuilder -> serverBuilder.registerDefaults()
                .withJsonConverter(jacksonConverter)
                .withStringConverter(stringConverter);
    }
}
