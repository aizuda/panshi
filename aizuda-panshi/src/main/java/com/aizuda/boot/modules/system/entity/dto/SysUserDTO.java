/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity.dto;

import com.aizuda.boot.modules.system.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户参数
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
public class SysUserDTO extends SysUser {

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "部门ID列表")
    private List<Long> departmentIds;

}
