/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.core.bean;

import com.aizuda.core.validation.Create;
import com.aizuda.core.validation.Update;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 基础类
 *
 * @author 青苗
 * @since 1.1.0
 */
@Setter
@Getter
public class SuperEntity implements BeanConvert {

    /**
     * 主键
     */
    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;

}
