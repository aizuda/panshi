/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分配角色资源
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
public class RoleResourceDTO {

    @Schema(description = "角色ID")
    @NotNull
    private Long roleId;

    @Schema(description = "资源ID列表")
    private List<Long> resourceIds;

}
