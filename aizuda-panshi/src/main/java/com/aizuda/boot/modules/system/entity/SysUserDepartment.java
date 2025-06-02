/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity;

import com.aizuda.core.bean.SuperEntity;
import com.aizuda.core.validation.Create;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户部门
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysUserDepartment", description = "系统用户部门")
public class SysUserDepartment extends SuperEntity {

    @Schema(description = "用户ID")
    @NotNull(groups = Create.class)
    @PositiveOrZero
    private Long userId;

    @Schema(description = "部门ID")
    @NotNull(groups = Create.class)
    @PositiveOrZero
    private Long departmentId;

}
