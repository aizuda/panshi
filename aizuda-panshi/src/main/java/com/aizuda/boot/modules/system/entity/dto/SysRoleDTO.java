package com.aizuda.boot.modules.system.entity.dto;

import com.aizuda.boot.modules.system.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 系统角色DTO
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
public class SysRoleDTO extends SysRole {

    @Schema(description = "资源ID列表")
    private List<Long> resourceIds;

}
