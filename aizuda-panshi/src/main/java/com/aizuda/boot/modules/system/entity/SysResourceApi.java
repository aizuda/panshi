/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity;

import com.aizuda.core.bean.SuperEntity;
import com.aizuda.core.validation.Create;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源接口
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "SysResourceApi", description = "系统资源")
public class SysResourceApi extends SuperEntity {

	@Schema(description = "资源ID")
	@NotNull(groups = Create.class)
	@PositiveOrZero
	private Long resourceId;

	@Schema(description = "编码")
	@NotEmpty
	@Size(max = 100)
	private String code;

	@Schema(description = "备注")
	@Size(max = 255)
	private String remark;

}
