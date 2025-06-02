package com.aizuda.boot.modules.system.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysUserRelIdsVO {

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "部门ID列表")
    private List<Long> departmentIds;

}
