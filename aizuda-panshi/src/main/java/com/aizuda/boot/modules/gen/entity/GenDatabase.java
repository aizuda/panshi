package com.aizuda.boot.modules.gen.entity;

import com.aizuda.core.bean.BaseEntity;
import com.aizuda.core.validation.Create;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 代码生成数据源表
 *
 * @author 青苗
 * @since 1.0.0
 */
@Getter
@Setter
@Schema(name = "GenDatabase", description = "代码生成数据源表")
@TableName("gen_database")
public class GenDatabase extends BaseEntity {

	@Schema(description = "类型")
	@NotBlank(groups = Create.class)
	@Size(max = 50)
	private String type;

	@Schema(description = "别名")
	@NotBlank(groups = Create.class)
	@Size(max = 50)
	private String alias;

	@Schema(description = "用户名")
	@NotBlank(groups = Create.class)
	@Size(max = 50)
	private String username;

	@Schema(description = "密码")
	@NotBlank(groups = Create.class)
	@Size(max = 50)
	private String password;

	@Schema(description = "端口")
	@NotNull(groups = Create.class)
	@PositiveOrZero
	private Integer port;

	@Schema(description = "主机")
	@NotBlank(groups = Create.class)
	@Size(max = 50)
	private String host;

	@Schema(description = "数据库名")
	@NotBlank(groups = Create.class)
	@Size(max = 50)
	private String dbName;

	@Schema(description = "备注")
	@Size(max = 255)
	private String remark;

	public String driverClassName() {
		return "org.postgresql.Driver";
	}

	public String url() {
		DbType dbType = this.dbType();
		StringBuilder url = new StringBuilder();
		if (dbType == DbType.MYSQL) {
			url.append("jdbc:mysql://");
		} else if (dbType == DbType.ORACLE) {
			url.append("jdbc:oracle:thin:@//");
		} else {
			url.append("jdbc:postgresql://");
		}
		url.append(this.host).append(":").append(this.port);
		url.append("/").append(this.dbName);
		if (dbType == DbType.MYSQL) {
			url.append("?useUnicode=true&characterEncoding=UTF-8&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai");
		}
		return url.toString();
	}

	/**
	 * 数据库类型判断
	 */
	public DbType dbType() {
		if (null != type) {
			if (type.contains("mysql")) {
				return DbType.MYSQL;
			}
			if (type.contains("oracle")) {
				return DbType.ORACLE;
			}
		}
		return DbType.POSTGRE_SQL;
	}
}
