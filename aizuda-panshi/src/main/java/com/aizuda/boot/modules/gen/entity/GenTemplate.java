package com.aizuda.boot.modules.gen.entity;

import com.aizuda.core.bean.BaseEntity;
import com.aizuda.core.validation.Create;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 代码生成模板表
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "GenTemplate", description = "代码生成模板表")
@TableName("gen_template")
public class GenTemplate extends BaseEntity {

	@Schema(description = "模板名称")
	@NotBlank(groups = Create.class)
	@Size(max = 100)
	private String tplName;

	@Schema(description = "模板内容")
	@NotBlank(groups = Create.class)
	private String tplContent;

	@Schema(description = "输出文件")
	@NotBlank(groups = Create.class)
	@Size(max = 255)
	private String outFile;

	@Schema(description = "模板描述")
	@Size(max = 255)
	private String remark;

}
