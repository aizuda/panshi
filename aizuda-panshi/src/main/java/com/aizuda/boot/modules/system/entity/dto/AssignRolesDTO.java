/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分配角色参数，多用户分配多角色
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
public class AssignRolesDTO {

    @Schema(description = "用户ID列表")
    @NotEmpty
    private List<Long> userIds;

    @Schema(description = "角色ID列表")
    @NotEmpty
    private List<Long> roleIds;

}
