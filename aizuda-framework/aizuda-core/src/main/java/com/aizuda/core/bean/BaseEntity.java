/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.core.bean;

import com.aizuda.core.ApiConstants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 业务框架基础实体
 *
 * @author 青苗
 * @since 1.1.0
 */
@Setter
@Getter
public class BaseEntity extends SuperEntity {

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    protected String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = ApiConstants.DATE_MM)
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.UPDATE)
    protected String updateBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = ApiConstants.DATE_MM)
    @TableField(fill = FieldFill.UPDATE)
    protected Date updateTime;

    /**
     * 删除 0、否 1、是
     */
    @JsonIgnore
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;


}
