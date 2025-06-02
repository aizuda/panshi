/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity.vo;

import com.aizuda.boot.modules.system.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysUserVO extends SysUser {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "所属角色多个英文逗号分割")
    private String roles;

}
