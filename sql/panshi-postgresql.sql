
-- ----------------------------
-- Table structure for gen_database
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_database";
CREATE TABLE "public"."gen_database" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "alias" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "password" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "port" int4 NOT NULL,
    "host" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "db_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."gen_database"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."gen_database"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."gen_database"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."gen_database"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."gen_database"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."gen_database"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."gen_database"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."gen_database"."type" IS '类型';
COMMENT ON COLUMN "public"."gen_database"."alias" IS '别名';
COMMENT ON COLUMN "public"."gen_database"."username" IS '用户名';
COMMENT ON COLUMN "public"."gen_database"."password" IS '密码';
COMMENT ON COLUMN "public"."gen_database"."port" IS '端口';
COMMENT ON COLUMN "public"."gen_database"."host" IS '主机';
COMMENT ON COLUMN "public"."gen_database"."db_name" IS '数据库名';
COMMENT ON COLUMN "public"."gen_database"."remark" IS '备注';
COMMENT ON TABLE "public"."gen_database" IS '代码生成数据源表';

-- ----------------------------
-- Records of gen_database
-- ----------------------------
INSERT INTO "public"."gen_database" VALUES (1829328106278948865, 0, 'admin', '2024-08-30 09:19:42.467', NULL, NULL, 0, 'pgsql', 'aizuda', 'postgres', '123456', 5432, '127.0.0.1', 'aizuda-boot', NULL);

-- ----------------------------
-- Table structure for gen_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_template";
CREATE TABLE "public"."gen_template" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "tpl_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "tpl_content" text COLLATE "pg_catalog"."default" NOT NULL,
    "out_file" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."gen_template"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."gen_template"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."gen_template"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."gen_template"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."gen_template"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."gen_template"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."gen_template"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."gen_template"."tpl_name" IS '模板名称';
COMMENT ON COLUMN "public"."gen_template"."tpl_content" IS '模板内容';
COMMENT ON COLUMN "public"."gen_template"."out_file" IS '输出文件';
COMMENT ON COLUMN "public"."gen_template"."remark" IS '模板描述';
COMMENT ON TABLE "public"."gen_template" IS '代码生成模板表';

-- ----------------------------
-- Records of gen_template
-- ----------------------------
INSERT INTO "public"."gen_template" VALUES (1839920582442209282, 0, 'admin', '2024-09-28 14:50:25.582', 'admin', '2024-10-13 15:51:31.568', 0, '实体', 'package ${package.Entity};

#foreach($pkg in ${table.importPackages})
import ${pkg};
#end
#if(${entityLombokModel})

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.Getter;
#end

/**
 * $!{table.comment}
 *
 * @author ${author}
 * @since ${date}
 */
#if(${entityLombokModel})
@Getter
@Setter
@Schema(name = "${entity}", description = "$!{table.comment}")
#end
#if(${table.convert})
@TableName("${table.name}")
#end
#if(${superEntityClass})
public class ${entity} extends ${superEntityClass} {
#else
public class ${entity} implements Serializable {
#end
#foreach($field in ${table.fields})
#if(${field.keyFlag})
#set($keyPropertyName=${field.propertyName})
#end
#if("$!field.comment" != "")

	@Schema(description = "${field.comment}")
#end
#if(${field.keyFlag})
#if(${field.keyIdentityFlag})
	@TableId(value="${field.name}", type= IdType.AUTO)
#elseif(${field.convert})
    @TableId("${field.name}")
#end
#elseif(${field.fill})
#if(${field.convert})
	@TableField(value = "${field.name}", fill = FieldFill.${field.fill})
#else
	@TableField(fill = FieldFill.${field.fill})
#end
#elseif(${field.convert})
	@TableField("${field.name}")
#end
#if(${logicDeleteFieldName}==${field.name})
	@TableLogic
#end
${field.customMap.validationAnnotations}private ${field.propertyType} ${field.propertyName};
#end

#if(!${entityLombokModel})
#foreach($field in ${table.fields})
#if(${field.propertyType.equals("Boolean")})
#set($getprefix="is")
#else
#set($getprefix="get")
#end

	public ${field.propertyType} ${getprefix}${field.capitalName}() {
		return ${field.propertyName};
	}

#if(${entityBuilderModel})
	public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
#else
	public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
#end
		this.${field.propertyName} = ${field.propertyName};
#if(${entityBuilderModel})
		return this;
#end
	}
#end
#end
}
', '%s.java', '实体模板');
INSERT INTO "public"."gen_template" VALUES (1829399584248733698, 0, 'admin', '2024-08-30 14:03:44.143', 'admin', '2024-10-13 16:43:28.284', 0, '测试模板', '', 'mapper/%sMapper.xml', '123');

-- ----------------------------
-- Table structure for sys_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_app";
CREATE TABLE "public"."sys_app" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "identification" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "secret_key" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 0,
    "status" int2 NOT NULL DEFAULT 1,
    "expire" timestamp(0) NOT NULL,
    "sort" int2 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."sys_app"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_app"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_app"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_app"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_app"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_app"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_app"."identification" IS '标识';
COMMENT ON COLUMN "public"."sys_app"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_app"."secret_key" IS '密钥';
COMMENT ON COLUMN "public"."sys_app"."status" IS '状态 0、禁用 1、正常';
COMMENT ON COLUMN "public"."sys_app"."expire" IS '授权到期';
COMMENT ON COLUMN "public"."sys_app"."sort" IS '排序';
COMMENT ON TABLE "public"."sys_app" IS '应用';

-- ----------------------------
-- Records of sys_app
-- ----------------------------
INSERT INTO "public"."sys_app" VALUES (1682692363772092417, 0, 'admin', '2023-07-22 18:01:00', 'admin', '2023-12-30 12:42:14.796', 0, 'ToTeet', 'toTeet', '122121', 1, '2023-07-25 18:01:23', 1);
INSERT INTO "public"."sys_app" VALUES (1688820205836636161, 0, 'admin', '2023-08-08 15:51:28.49', 'admin', '2023-12-30 12:42:20.268', 0, 'hiAzd', 'hiAzd', '123123', 0, '2023-08-09 15:51:22', 1);

-- ----------------------------
-- Table structure for sys_configure
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_configure";
CREATE TABLE "public"."sys_configure" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "category" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "keyword" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "content" text COLLATE "pg_catalog"."default" NOT NULL DEFAULT 0,
    "title" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "sort" int2 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."sys_configure"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_configure"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_configure"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_configure"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_configure"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_configure"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_configure"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_configure"."category" IS '分类';
COMMENT ON COLUMN "public"."sys_configure"."keyword" IS '关键字';
COMMENT ON COLUMN "public"."sys_configure"."content" IS '内容';
COMMENT ON COLUMN "public"."sys_configure"."title" IS '标题';
COMMENT ON COLUMN "public"."sys_configure"."sort" IS '排序';
COMMENT ON TABLE "public"."sys_configure" IS '扩展配置';

-- ----------------------------
-- Records of sys_configure
-- ----------------------------
INSERT INTO "public"."sys_configure" VALUES (1495062995721515010, 0, 'admin', '2022-02-19 23:49:00', 'admin', '2023-12-30 12:44:12.577', 0, '系统参数', 'msgConfig', 'mobile=1', '短信配置', 2);
INSERT INTO "public"."sys_configure" VALUES (1495063397821050881, 0, 'admin', '2022-02-19 23:51:00', 'admin', '2023-08-29 21:03:35.338', 0, '业务参数', 'codeSwitch', 'off', '发送验证码开关', 6);

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_department";
CREATE TABLE "public"."sys_department" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "pid" int8 NOT NULL DEFAULT 0,
    "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "sort" int2 NOT NULL DEFAULT 0,
    "remark" varchar(255) COLLATE "pg_catalog"."default",
    "status" int2 NOT NULL DEFAULT 1,
    "head_id" int8,
    "head_name" varchar(100) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_department"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_department"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_department"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_department"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_department"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_department"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_department"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_department"."pid" IS '父ID';
COMMENT ON COLUMN "public"."sys_department"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_department"."code" IS '编码';
COMMENT ON COLUMN "public"."sys_department"."sort" IS '排序';
COMMENT ON COLUMN "public"."sys_department"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_department"."status" IS '状态 0、禁用 1、正常';
COMMENT ON COLUMN "public"."sys_department"."head_id" IS '主管ID';
COMMENT ON COLUMN "public"."sys_department"."head_name" IS '主管名称';
COMMENT ON TABLE "public"."sys_department" IS '部门';

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO "public"."sys_department" VALUES (1, 0, 'admin', '2022-02-20 22:19:00', 'admin', '2023-12-30 14:23:47.158', 0, 2, '开发 A 组', '2020', 1, '开发 A 组', 1, NULL, NULL);
INSERT INTO "public"."sys_department" VALUES (1696346580223049729, 0, 'admin', '2023-08-29 10:18:35.978', 'admin', '2023-12-30 14:23:51.702', 0, 2, '开发 B 组', '2010', 1, '开发 B 组', 1, NULL, NULL);
INSERT INTO "public"."sys_department" VALUES (1778237792399392769, 0, 'admin', '2024-04-11 09:45:02.425', NULL, NULL, 0, 3, '财务部', '0023', 4, '财务部', 0, 1778236187025342466, NULL);
INSERT INTO "public"."sys_department" VALUES (1778246587070545922, 0, 'admin', '2024-04-11 10:19:59.239', NULL, NULL, 0, 3, '综合管理部', 'zongheguanli', 1, '综合管理', 1, 1778236187025342466, '部门领导');
INSERT INTO "public"."sys_department" VALUES (2, 0, 'admin', '2021-11-07 10:13:54', 'admin', '2024-04-11 10:22:46.956', 0, 3, '产品部', '2000', 0, '产品部', 1, 1778236021094481921, '杨小凤');
INSERT INTO "public"."sys_department" VALUES (1778247413147107329, 0, 'admin', '2024-04-11 10:23:16.19', NULL, NULL, 0, 3, '应用部', 'yingyongbu', 2, '应用部', 1, 1778236187025342466, '夏小华');
INSERT INTO "public"."sys_department" VALUES (1778247724637093889, 0, 'admin', '2024-04-11 10:24:30.455', NULL, NULL, 0, 1778246587070545922, '人事部', 'renshibu', 1, '人事部', 1, 1778235419958444034, '出纳');
INSERT INTO "public"."sys_department" VALUES (3, 0, 'admin', '2022-02-20 22:21:20', 'admin', '2024-05-02 13:18:54.14', 0, 0, '飞龙驱动科技', '1000', 2, '太阳科技', 1, 0, 'CEO');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict";
CREATE TABLE "public"."sys_dict" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "pid" int8 NOT NULL DEFAULT 0,
    "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "content" text COLLATE "pg_catalog"."default",
    "status" int2 NOT NULL DEFAULT 1,
    "sort" int2 NOT NULL DEFAULT 0,
    "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_dict"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_dict"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_dict"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_dict"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dict"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_dict"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_dict"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_dict"."pid" IS '父ID';
COMMENT ON COLUMN "public"."sys_dict"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_dict"."code" IS '编码';
COMMENT ON COLUMN "public"."sys_dict"."content" IS '内容';
COMMENT ON COLUMN "public"."sys_dict"."status" IS '状态 0、禁用 1、正常';
COMMENT ON COLUMN "public"."sys_dict"."sort" IS '排序';
COMMENT ON COLUMN "public"."sys_dict"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_dict" IS '系统字典';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO "public"."sys_dict" VALUES (1479120984875122689, 0, 'admin', '2022-01-07 00:01:38.195', 'admin', '2023-07-21 15:23:57.83', 0, 0, '会员类型', 'memberType', NULL, 1, 13, '会员类型');
INSERT INTO "public"."sys_dict" VALUES (1682657662327971842, 0, 'admin', '2023-07-22 15:43:43.623', NULL, NULL, 0, 0, '测试字典', 'testDic', NULL, 1, 12, '');
INSERT INTO "public"."sys_dict" VALUES (1479121109454340098, 0, 'admin', '2022-01-07 00:02:07.897', 'admin', '2023-12-30 18:25:41.333', 0, 0, '消息类型', 'msgType', NULL, 1, 110, '消息类型');
INSERT INTO "public"."sys_dict" VALUES (1480140709017047042, 0, 'admin', '2022-01-09 19:33:00', 'admin', '2023-12-30 18:23:28.926', 0, 1479120984875122689, '普通会员', '100', '1', 1, 1, '普通会员');
INSERT INTO "public"."sys_dict" VALUES (1741777618865938433, 0, 'admin', '2024-01-01 19:05:19.667', NULL, NULL, 0, 1479120984875122689, '黄金会员', '102', '2', 1, 1, NULL);
INSERT INTO "public"."sys_dict" VALUES (1741778332312854529, 0, 'admin', '2024-01-01 19:08:09.758', NULL, NULL, 0, 1479120984875122689, '铂金会员', '101', '3', 1, 1, NULL);
INSERT INTO "public"."sys_dict" VALUES (1688816911709966337, 0, 'admin', '2023-08-08 15:38:00', 'admin', '2023-12-30 18:26:50.354', 0, 1479121109454340098, '系统', '100', '1', 1, 1, '系统消息');
INSERT INTO "public"."sys_dict" VALUES (1481293017771884546, 0, 'admin', '2022-01-12 23:52:31.189', 'admin', '2023-12-30 18:27:13.896', 0, 1479121109454340098, '邮箱', 'email', '2', 1, 2, '发送邮件');
INSERT INTO "public"."sys_dict" VALUES (1481293159988150273, 0, 'admin', '2022-01-12 23:53:00', 'admin', '2023-12-30 18:27:23.947', 0, 1479121109454340098, '短信', '101', '3', 1, 2, '发送短信');
INSERT INTO "public"."sys_dict" VALUES (1696509959562641410, 0, 'admin', '2023-08-29 21:07:48.647', 'admin', '2023-12-30 18:29:16.108', 0, 1682657662327971842, 'test', '11', '1', 1, 11, NULL);
INSERT INTO "public"."sys_dict" VALUES (1696509851961966594, 0, 'admin', '2023-08-29 21:07:22.994', 'admin', '2023-12-30 18:28:12.481', 0, 1682657662327971842, 'key1', '33', '2', 0, 3, NULL);

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_message";
CREATE TABLE "public"."sys_message" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "title" varchar(255) COLLATE "pg_catalog"."default",
    "content" varchar(800) COLLATE "pg_catalog"."default",
    "category" int2 NOT NULL DEFAULT 0,
    "business_id" int8,
    "business_type" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_message"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_message"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_message"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_message"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_message"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_message"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_message"."title" IS '标题';
COMMENT ON COLUMN "public"."sys_message"."content" IS '内容';
COMMENT ON COLUMN "public"."sys_message"."category" IS '类别 0，通知 1，消息 2，待办';
COMMENT ON COLUMN "public"."sys_message"."business_id" IS '业务ID';
COMMENT ON COLUMN "public"."sys_message"."business_type" IS '业务类型';
COMMENT ON TABLE "public"."sys_message" IS '系统消息表';

-- ----------------------------
-- Records of sys_message
-- ----------------------------
INSERT INTO "public"."sys_message" VALUES (1709154710567243777, 0, 'admin', '2023-10-03 18:33:32.219', 'admin', '2023-10-12 08:35:34.241', '待办 2', '待办 2 内容', 2, NULL, NULL);
INSERT INTO "public"."sys_message" VALUES (1709154768926789634, 0, 'admin', '2023-10-03 18:33:46.133', 'admin', '2023-12-21 20:50:22.154', '待办 5', '待办 5 内容', 2, NULL, NULL);
INSERT INTO "public"."sys_message" VALUES (1709154417339256833, 0, 'admin', '2023-10-03 18:32:22.312', 'admin', '2023-12-27 20:18:51.916', '通知 6', '通知 6 内容', 0, NULL, NULL);
INSERT INTO "public"."sys_message" VALUES (1835629624119488513, 0, 'admin', '2024-09-16 18:39:41.396', NULL, NULL, '流程：测试业务流程（采购单） 待审批', '流程：测试业务流程（采购单） 待审批 ，当前所在节点：审核人 ，任务发起人：admin', 2, 1835629624023019522, 'flowTask');
INSERT INTO "public"."sys_message" VALUES (1844375699767443457, 0, 'admin', '2024-10-10 21:53:28.315', NULL, NULL, '流程：自选审批 待审批', '流程：自选审批 待审批 ，当前所在节点：审核人 ，任务发起人：admin', 2, 1844375699075383297, 'flowTodoTask');
INSERT INTO "public"."sys_message" VALUES (1844385185366396929, 1778248547756670978, 'test05', '2024-10-10 22:31:09.868', NULL, NULL, '流程：自选审批 待审批', '流程：自选审批 待审批 ，当前所在节点：审核人 ，任务发起人：test05', 2, 1844375699075383297, 'flowTodoTask');

-- ----------------------------
-- Table structure for sys_message_receiver
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_message_receiver";
CREATE TABLE "public"."sys_message_receiver" (
    "id" int8 NOT NULL,
    "message_id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "viewed" int2 NOT NULL DEFAULT 0,
    "send_status" int2 NOT NULL DEFAULT 0,
    "send_failure" varchar(255) COLLATE "pg_catalog"."default",
    "send_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_message_receiver"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_message_receiver"."message_id" IS '消息ID';
COMMENT ON COLUMN "public"."sys_message_receiver"."user_id" IS '接收人ID';
COMMENT ON COLUMN "public"."sys_message_receiver"."viewed" IS '已查看 0，否 1，是';
COMMENT ON COLUMN "public"."sys_message_receiver"."send_status" IS '发送状态 0，未发送 1，成功 2，失败';
COMMENT ON COLUMN "public"."sys_message_receiver"."send_failure" IS '发送失败原因';
COMMENT ON COLUMN "public"."sys_message_receiver"."send_time" IS '发送时间';
COMMENT ON TABLE "public"."sys_message_receiver" IS '消息接收人表';

-- ----------------------------
-- Records of sys_message_receiver
-- ----------------------------
INSERT INTO "public"."sys_message_receiver" VALUES (1844375699863912449, 1844375699767443457, 1778248547756670978, 0, 2, '未上线', '2024-10-10 21:53:28.336');
INSERT INTO "public"."sys_message_receiver" VALUES (1844385185366396930, 1844385185366396929, 1778264912529981441, 0, 2, '未上线', '2024-10-10 22:31:09.868');
INSERT INTO "public"."sys_message_receiver" VALUES (1709154768926789634, 1709154768926789634, 0, 1, 0, NULL, NULL);
INSERT INTO "public"."sys_message_receiver" VALUES (1709154417339256833, 1709154417339256833, 0, 1, 0, NULL, NULL);
INSERT INTO "public"."sys_message_receiver" VALUES (1709154710567243777, 1709154710567243777, 0, 1, 0, NULL, NULL);
INSERT INTO "public"."sys_message_receiver" VALUES (1835629019904819202, 1835629019837710338, 0, 1, 1, NULL, '2024-09-16 18:37:17.34');
INSERT INTO "public"."sys_message_receiver" VALUES (1835629624169820162, 1835629624119488513, 0, 1, 1, NULL, '2024-09-16 18:39:41.408');
INSERT INTO "public"."sys_message_receiver" VALUES (1844720796367376386, 1844720796300267521, 0, 1, 1, NULL, '2024-10-11 20:44:45.771');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_post";
CREATE TABLE "public"."sys_post" (
                                     "id" int8 NOT NULL,
                                     "create_id" int8 NOT NULL,
                                     "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
                                     "create_time" timestamp(6) NOT NULL,
                                     "update_by" varchar(30) COLLATE "pg_catalog"."default",
                                     "update_time" timestamp(6),
                                     "deleted" int2 NOT NULL DEFAULT 0,
                                     "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
                                     "code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
                                     "sort" int2 NOT NULL DEFAULT 0,
                                     "status" int2 NOT NULL DEFAULT 1,
                                     "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_post"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_post"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_post"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_post"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_post"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_post"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_post"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_post"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_post"."code" IS '编码';
COMMENT ON COLUMN "public"."sys_post"."sort" IS '排序';
COMMENT ON COLUMN "public"."sys_post"."status" IS '状态 0、禁用 1、正常';
COMMENT ON COLUMN "public"."sys_post"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_post" IS '岗位';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO "public"."sys_post" VALUES (1494699404912701442, 0, 'admin', '2022-02-18 23:44:42.96', 'admin', '2023-12-30 12:19:47.468', 0, '财务经理', '1100', 1, 1, '公司财务负责人');
INSERT INTO "public"."sys_post" VALUES (1695006999871537153, 0, 'admin', '2023-08-25 17:35:35.134', 'admin', '2023-12-30 12:21:55.376', 0, '董事高管', '1000', 5, 1, '公司管理层');
INSERT INTO "public"."sys_post" VALUES (1494699370099978242, 0, 'admin', '2022-02-18 23:44:00', 'admin', '2023-12-30 12:22:46.373', 0, '测试人员', '3100', 3, 1, '测试岗位');
INSERT INTO "public"."sys_post" VALUES (1494699308443709442, 0, 'admin', '2022-02-18 23:44:00', 'admin', '2024-04-12 20:43:57.077', 0, '研发人员', '3000', 11, 1, '研发岗位');
INSERT INTO "public"."sys_post" VALUES (1740951504629825537, 0, 'admin', '2023-12-30 12:22:38.676', 'admin', '2024-04-15 20:37:12.702', 0, '部门负责人', '1300', 2, 0, '普通管理层');
INSERT INTO "public"."sys_post" VALUES (1740951042186838018, 0, 'admin', '2023-12-30 12:20:48.423', 'admin', '2024-08-22 22:18:45.992', 0, '人力经理', '2000', 3, 1, '人力资源负责人');

-- ----------------------------
-- Table structure for sys_region
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_region";
CREATE TABLE "public"."sys_region" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "pid" int8 NOT NULL DEFAULT 0,
    "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "sort" int2 NOT NULL DEFAULT 0,
    "level" int2 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_region"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_region"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_region"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_region"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_region"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_region"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_region"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_region"."pid" IS '父ID';
COMMENT ON COLUMN "public"."sys_region"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_region"."code" IS '编码';
COMMENT ON COLUMN "public"."sys_region"."sort" IS '排序';
COMMENT ON COLUMN "public"."sys_region"."level" IS '类型 0，省份直辖市 1，地市 2，区县';
COMMENT ON TABLE "public"."sys_region" IS '行政区域';

-- ----------------------------
-- Records of sys_region
-- ----------------------------
INSERT INTO "public"."sys_region" VALUES (1496875814116872194, 0, 'admin', '2022-02-24 23:52:59.356', NULL, NULL, 0, 1496872692367691778, '北京市', '110100', 1101, 1);
INSERT INTO "public"."sys_region" VALUES (1496875919859470338, 0, 'admin', '2022-02-24 23:53:24.563', NULL, NULL, 0, 1496875814116872194, '东城区', '110101', 1101, 2);
INSERT INTO "public"."sys_region" VALUES (1496889386024792065, 0, 'admin', '2022-02-25 00:46:55.153', NULL, NULL, 0, 1496875814116872194, '西城区', '110102', 110, 2);
INSERT INTO "public"."sys_region" VALUES (1496872692367691778, 0, 'admin', '2022-02-24 23:40:00', 'admin', '2023-07-27 10:29:55.006', 0, 1496875814116872194, '北京市', '1100', 1100, 2);
INSERT INTO "public"."sys_region" VALUES (1686549235646140418, 0, 'admin', '2023-08-02 09:27:27.005', 'admin', '2023-08-02 09:27:54.247', 0, 1686549235646140418, '测试 2', '123', 1, 2);
INSERT INTO "public"."sys_region" VALUES (1686637003596435458, 0, 'admin', '2023-08-02 15:16:12.514', 'admin', '2023-08-02 15:20:44.913', 0, 1686637003596435458, '区域 1-1', '3', 1, 2);
INSERT INTO "public"."sys_region" VALUES (1686638313905393666, 0, 'admin', '2023-08-02 15:21:24.916', 'admin', '2023-08-02 15:21:42.49', 0, 1686638313905393666, '区域 1-1', '1', 1, 2);
INSERT INTO "public"."sys_region" VALUES (1686636956955774977, 0, 'admin', '2023-08-02 15:16:01.394', 'admin', '2023-08-02 15:24:52.263', 0, 1686636956955774977, '区域1', ' 1', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1686639222450360322, 0, 'admin', '2023-08-02 15:25:00', 'admin', '2023-12-30 15:02:26.114', 0, 0, '北京市', '1000', 1, 0);
INSERT INTO "public"."sys_region" VALUES (1686639321326882818, 0, 'admin', '2023-08-02 15:25:25.105', 'admin', '2023-12-30 15:02:56.383', 0, 1686639222450360322, '海淀区', '1001', 3, 1);
INSERT INTO "public"."sys_region" VALUES (1686657764474294273, 0, 'admin', '2023-08-02 15:25:00', 'admin', '2023-12-30 15:03:05.492', 0, 1686639222450360322, '东城区', '1002', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1740999425233948674, 0, 'admin', '2023-12-30 15:33:03.837', 'admin', '2023-12-30 15:33:28.847', 0, 0, '山东省', '2000', 1, 0);
INSERT INTO "public"."sys_region" VALUES (1740999601650569217, 0, 'admin', '2023-12-30 15:33:45.902', NULL, NULL, 0, 1740999425233948674, '济南市', '2001', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1740999655740313601, 0, 'admin', '2023-12-30 15:33:58.795', NULL, NULL, 0, 1740999425233948674, '青岛市', '2002', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1740999720475201538, 0, 'admin', '2023-12-30 15:34:14.229', NULL, NULL, 0, 1740999425233948674, '烟台市', '2003', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1740999789739937794, 0, 'admin', '2023-12-30 15:34:30.745', NULL, NULL, 0, 1686639222450360322, '朝阳区', '1003', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1740999940604858369, 0, 'admin', '2023-12-30 15:35:06.711', NULL, NULL, 0, 0, '湖北省', '3000', 1, 0);
INSERT INTO "public"."sys_region" VALUES (1740999994380029954, 0, 'admin', '2023-12-30 15:35:19.533', NULL, NULL, 0, 1740999940604858369, '武汉市', '3001', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741000516059172866, 0, 'admin', '2023-12-30 15:37:23.919', NULL, NULL, 0, 1740999940604858369, '黄石市', '3002', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741000624054112257, 0, 'admin', '2023-12-30 15:37:49.669', NULL, NULL, 0, 1740999940604858369, '襄阳市', '3003', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741000786356899841, 0, 'admin', '2023-12-30 15:38:28.363', NULL, NULL, 0, 0, '河南省', '4000', 1, 0);
INSERT INTO "public"."sys_region" VALUES (1741000913591111681, 0, 'admin', '2023-12-30 15:38:58.7', NULL, NULL, 0, 1741000786356899841, '郑州市', '4001', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741000974114918402, 0, 'admin', '2023-12-30 15:39:13.132', NULL, NULL, 0, 1741000786356899841, '开封市', '4002', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741001078536310786, 0, 'admin', '2023-12-30 15:39:38.019', NULL, NULL, 0, 1741000786356899841, '洛阳市', '3003', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741001213450293249, 0, 'admin', '2023-12-30 15:40:10.195', NULL, NULL, 0, 0, '江西省', '5000', 1, 0);
INSERT INTO "public"."sys_region" VALUES (1741001313794822146, 0, 'admin', '2023-12-30 15:40:34.116', NULL, NULL, 0, 1741001213450293249, '南昌市', '5001', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741001528815816705, 0, 'admin', '2023-12-30 15:41:25.376', NULL, NULL, 0, 1741001213450293249, '抚州市', '5003', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741001430413250562, 0, 'admin', '2023-12-30 15:41:01.917', 'admin', '2023-12-30 15:41:31.507', 0, 1741001213450293249, '赣州市', '5002', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741001669568270338, 0, 'admin', '2023-12-30 15:41:58.938', NULL, NULL, 0, 0, '湖南省', '6000', 1, 0);
INSERT INTO "public"."sys_region" VALUES (1741001777865199617, 0, 'admin', '2023-12-30 15:42:24.757', NULL, NULL, 0, 1741001669568270338, '长沙市', '6001', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741001843342479362, 0, 'admin', '2023-12-30 15:42:40.364', NULL, NULL, 0, 1741001669568270338, '株洲市', '6002', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1741001905950855169, 0, 'admin', '2023-12-30 15:42:55.299', NULL, NULL, 0, 1741001669568270338, '湘潭市', '6003', 1, 1);
INSERT INTO "public"."sys_region" VALUES (1763042934416281601, 0, 'admin', '2024-02-29 11:26:05.976', NULL, NULL, 0, 1741000516059172866, '鼎湖山', '3002001', 1, 2);

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_resource";
CREATE TABLE "public"."sys_resource" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "pid" int8 NOT NULL DEFAULT 0,
    "title" varchar(60) COLLATE "pg_catalog"."default" NOT NULL,
    "alias" varchar(60) COLLATE "pg_catalog"."default",
    "type" int2 NOT NULL DEFAULT 0,
    "code" varchar(100) COLLATE "pg_catalog"."default",
    "redirect" varchar(100) COLLATE "pg_catalog"."default",
    "path" varchar(100) COLLATE "pg_catalog"."default",
    "icon" varchar(30) COLLATE "pg_catalog"."default",
    "status" int2 NOT NULL DEFAULT 1,
    "sort" int4 NOT NULL DEFAULT 0,
    "component" varchar(255) COLLATE "pg_catalog"."default",
    "color" varchar(30) COLLATE "pg_catalog"."default",
    "hidden" bool NOT NULL DEFAULT false,
    "parent_route" varchar(255) COLLATE "pg_catalog"."default",
    "keep_alive" bool NOT NULL DEFAULT false,
    "query" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_resource"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_resource"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_resource"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_resource"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_resource"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_resource"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_resource"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_resource"."pid" IS '父ID';
COMMENT ON COLUMN "public"."sys_resource"."title" IS '名称';
COMMENT ON COLUMN "public"."sys_resource"."alias" IS '别名';
COMMENT ON COLUMN "public"."sys_resource"."type" IS '类型 0，菜单 1，iframe 2，外链 3，按钮';
COMMENT ON COLUMN "public"."sys_resource"."code" IS '编码';
COMMENT ON COLUMN "public"."sys_resource"."redirect" IS '重定向';
COMMENT ON COLUMN "public"."sys_resource"."path" IS '文件路径';
COMMENT ON COLUMN "public"."sys_resource"."icon" IS '图标';
COMMENT ON COLUMN "public"."sys_resource"."status" IS '状态 0、禁用 1、正常';
COMMENT ON COLUMN "public"."sys_resource"."sort" IS '排序';
COMMENT ON COLUMN "public"."sys_resource"."component" IS '视图';
COMMENT ON COLUMN "public"."sys_resource"."color" IS '颜色';
COMMENT ON COLUMN "public"."sys_resource"."hidden" IS '隐藏菜单';
COMMENT ON COLUMN "public"."sys_resource"."parent_route" IS '上级路由';
COMMENT ON COLUMN "public"."sys_resource"."keep_alive" IS '保留查询参数';
COMMENT ON COLUMN "public"."sys_resource"."query" IS '查询携带参数';
COMMENT ON TABLE "public"."sys_resource" IS '系统资源';

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO "public"."sys_resource" VALUES (1705403264375562242, 0, 'admin', '2023-09-23 10:06:00', 'admin', '2024-08-10 18:38:36.947', 0, 1705397288498995201, '用户管理', 'user', 0, NULL, NULL, '/user', 'ep:user', 1, 90, 'setting_user', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705397288498995201, 0, 'admin', '2023-09-23 09:42:00', 'admin', '2024-07-31 11:24:41.863', 0, 0, '系统设置', 'setting', 0, NULL, NULL, '/setting', 'icon-swagger', 1, 10, 'layout.base', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705403612024643585, 0, 'admin', '2023-09-23 10:08:00', 'admin', '2024-08-11 12:35:34.497', 0, 1705397288498995201, '岗位管理', 'post', 0, NULL, NULL, '/post', 'icon-uv', 1, 80, 'setting_post', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705405860804927489, 0, 'admin', '2023-09-23 10:16:00', 'admin', '2024-08-12 14:11:19.676', 0, 1705397288498995201, '扩展配置', 'configure', 0, NULL, NULL, '/configure', 'icon-component', 1, 70, 'setting_configure', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705404534842826753, 0, 'admin', '2023-09-23 10:11:00', 'admin', '2024-08-12 14:41:22.99', 0, 1705397288498995201, '应用管理', 'app', 0, NULL, NULL, '/app', 'icon-goods-list', 1, 50, 'setting_app', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705405650779348993, 0, 'admin', '2023-09-23 10:16:00', 'admin', '2024-08-12 15:11:15.806', 0, 1705397288498995201, '行政区域', 'region', 0, NULL, NULL, '/region', 'icon-textarea', 1, 10, 'setting_region', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1734132265541500930, 0, 'admin', '2023-12-11 16:45:00', 'admin', '2024-08-12 19:05:05.492', 0, 1710476160198299650, '我的消息', 'my', 0, NULL, NULL, '/my', 'ep:message-box', 1, 10, 'message_my', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1710488066858471425, 0, 'admin', '2023-10-07 10:51:00', 'admin', '2024-08-12 19:05:23.803', 0, 1710476160198299650, '消息管理', 'list', 0, NULL, NULL, '/list', 'ep:chat-dot-square', 1, 20, 'message_list', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1818549132156563458, 0, 'admin', '2024-07-31 15:27:54.774', NULL, NULL, 0, 0, '首页', 'home', 0, NULL, NULL, '/home', 'mdi:monitor-dashboard', 1, 100, 'home', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705409192441257985, 0, 'admin', '2023-09-23 10:30:00', 'admin', '2023-09-23 10:30:00', 0, 1705397288498995201, '菜单管理', 'menu', 0, NULL, NULL, '/menu', 'ep:menu', 1, 20, 'setting_menu', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705405971014459394, 0, 'admin', '2023-09-23 10:17:00', 'admin', '2024-08-10 18:39:02.709', 0, 1705397288498995201, '字典管理', 'dict', 0, NULL, NULL, '/dict', 'ep:list', 1, 30, 'setting_dict', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705405518658772993, 0, 'admin', '2023-09-23 10:15:00', 'admin', '2024-08-07 19:35:45.939', 0, 1705397288498995201, '部门管理', 'department', 0, NULL, NULL, '/department', 'ep:wind-power', 1, 40, 'setting_department', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705404432669581313, 0, 'admin', '2023-09-23 10:11:00', 'admin', '2024-08-10 18:39:17.546', 0, 1705397288498995201, '角色管理', 'role', 0, NULL, NULL, '/role', 'icon-role', 1, 60, 'setting_role', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1710476160198299650, 0, 'admin', '2023-10-07 10:04:00', 'admin', '2024-08-12 19:05:41.119', 0, 0, '消息中心', 'message', 0, NULL, NULL, '/message', 'ep:message', 1, 20, 'layout.base', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1818497082743914497, 0, 'admin', '2024-07-31 12:01:05.231', 'admin', '2024-08-15 22:22:06.685', 0, 0, '外链测试菜单', 'link-test', 2, NULL, 'https://aizuda.com/home', '/link-test', 'icon-bug', 1, 5, 'iframe-page', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1818217918824460289, 0, 'admin', '2024-07-30 17:31:47.362', 'admin', '2024-08-15 22:22:11.254', 0, 0, 'Iframe 测试菜单', 'iframe-test', 1, NULL, 'https://aizuda.com/home', '/Iframe-test', 'icon-bug', 1, 3, 'iframe-page', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1843833708455759874, 1, 'admin', '2024-10-09 09:59:47.526', 'admin', '2024-10-09 09:59:54.8', 0, 0, '代码生成', 'gen', 0, NULL, NULL, '/gen', 'hugeicons:source-code-square', 1, 9, 'layout.base', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1843834040632053762, 1, 'admin', '2024-10-09 10:01:06.724', NULL, NULL, 0, 1843833708455759874, '数据源管理', 'gen_database', 0, NULL, NULL, '/gen/database', 'tabler:database', 1, 100, 'gen_database', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1843834117584949249, 1, 'admin', '2024-10-09 10:01:25.071', 'admin', '2024-10-09 10:01:34.656', 0, 1843833708455759874, '模板管理', 'gen_template', 0, NULL, NULL, '/gen/template', 'tabler:template', 1, 90, 'gen_template', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1799751555390029826, 0, 'admin', '2024-06-09 18:33:00', 'admin', '2025-06-02 13:58:38.293', 1, 1476568874467926018, '测试业务流程', 'business', 0, NULL, NULL, '/business', 'icon-bug', 1, 10, 'test_flow-business', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1792537175757885441, 0, 'admin', '2024-05-20 20:45:00', 'admin', '2025-06-02 13:58:44.322', 1, 1476568874467926018, '表单管理', 'form', 0, NULL, NULL, '/form', 'icon-build', 1, 30, 'flow_form', NULL, 'f', '', 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1705141694319927298, 0, 'admin', '2023-09-22 16:47:00', 'admin', '2025-06-02 13:58:54.556', 1, 1476568874467926018, '审批管理', 'group', 0, NULL, NULL, '/group', 'icon-component', 1, 30, 'flow_group', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1476568874467926018, 0, 'admin', '2021-12-30 23:00:00', 'admin', '2025-06-02 13:58:58.938', 1, 0, '审批管理', 'flow', 0, NULL, NULL, '/flow', 'ep:document', 1, 20, 'layout.base', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1731638949751033858, 0, 'admin', '2023-12-04 19:37:00', 'admin', '2025-06-02 13:59:04.161', 1, 1731637117037318145, '已审批', 'approved', 0, NULL, NULL, '/approved', 'ep:finished', 1, 10, 'approve_approved', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1735272762811854850, 0, 'admin', '2023-12-14 20:17:00', 'admin', '2025-06-02 13:59:08.055', 1, 1731637117037318145, '认领任务', 'pending-claim', 0, NULL, NULL, '/pending-claim', 'icon-receive', 1, 20, 'approve_pending-claim', NULL, 'f', 'claimTask', 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1731638597937008641, 0, 'admin', '2023-12-04 19:36:00', 'admin', '2025-06-02 13:59:12.877', 1, 1731637117037318145, '我收到的', 'my-received', 0, NULL, NULL, '/my-received', 'ep:user', 1, 30, 'approve_my-received', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1731638426658410498, 0, 'admin', '2023-12-04 19:35:00', 'admin', '2025-06-02 13:59:16.642', 1, 1731637117037318145, '我的申请', 'my-application', 0, NULL, NULL, '/my-application', 'icon-warehouse', 1, 40, 'approve_my-application', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1731637817964236801, 0, 'admin', '2023-12-04 19:33:00', 'admin', '2025-06-02 13:59:20.926', 1, 1731637117037318145, '待审批', 'pending-approval', 0, NULL, NULL, '/pending-approval', 'ep:memo', 1, 50, 'approve_pending-approval', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1734189772246941698, 0, 'admin', '2023-12-11 20:33:00', 'admin', '2025-06-02 13:59:26.196', 1, 1731637117037318145, '发起审批', 'launch', 0, NULL, NULL, '/launch', 'ep:pointer', 1, 60, 'approve_launch', NULL, 'f', '', 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1731637117037318145, 0, 'admin', '2023-12-04 19:30:00', 'admin', '2025-06-02 13:59:30.166', 1, 0, '流程审批', 'approve', 0, NULL, NULL, '/approve', 'ep:edit-pen', 1, 40, 'layout.base', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1824089134229397505, 0, 'admin', '2024-08-15 22:21:54.195', 'admin', '2025-06-02 14:05:54.426', 0, 0, '💎企业版', 'nui', 2, NULL, 'https://naiveui.aizuda.com/', '/nui', 'icon-warehouse', 1, 1, 'iframe-page', NULL, 'f', NULL, 'f', '{}');
INSERT INTO "public"."sys_resource" VALUES (1929418253438476290, 0, 'admin', '2025-06-02 14:02:13.136', 'admin', '2025-06-02 14:03:33.614', 1, 0, '企业版 NaiveUI 版本', 'nui', 2, NULL, 'https://naiveui.aizuda.com/', '/nui', 'icon-receive', 1, 1, 'iframe-page', NULL, 'f', NULL, 'f', '{}');

-- ----------------------------
-- Table structure for sys_resource_api
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_resource_api";
CREATE TABLE "public"."sys_resource_api" (
    "id" int8 NOT NULL,
    "resource_id" int8 NOT NULL,
    "code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_resource_api"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_resource_api"."resource_id" IS '资源ID';
COMMENT ON COLUMN "public"."sys_resource_api"."code" IS '权限编码';
COMMENT ON COLUMN "public"."sys_resource_api"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_resource_api" IS '系统资源接口';

-- ----------------------------
-- Records of sys_resource_api
-- ----------------------------
INSERT INTO "public"."sys_resource_api" VALUES (1883688099460403201, 1734132265541500930, 'sys:message:', '系统消息相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1883685354997272578, 1818549132156563458, 'sys:sse:connect', 'SSE连接');
INSERT INTO "public"."sys_resource_api" VALUES (1883688988191477762, 1705403264375562242, 'sys:department:listTree', '部门树列表');
INSERT INTO "public"."sys_resource_api" VALUES (1883693238829174786, 1705405518658772993, 'sys:user:page', '系统用户分页列表');
INSERT INTO "public"."sys_resource_api" VALUES (1884134174050689026, 1818549132156563458, 'sys:dict:listSelectOptions', '通过字典编码查询表单下拉选择项列表');
INSERT INTO "public"."sys_resource_api" VALUES (1884134613500502017, 1705409192441257985, 'sys:resource:', '系统资源相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1884133963064614914, 1705405971014459394, 'sys:dict:', '系统字典相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1883693031550865410, 1705405518658772993, 'sys:department:', '部门相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1883692520927907841, 1705404534842826753, 'sys:app:', '应用分页相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1883691408577515521, 1705404432669581313, 'sys:role:', '系统角色相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1883690965633847298, 1705405860804927489, 'sys:configure:', '扩展配置相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1883690404024930306, 1705403612024643585, 'sys:post:', '岗位相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1883689161240072193, 1705403264375562242, 'sys:user:', '系统用户相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1883687477814218753, 1710488066858471425, 'sys:message:', '系统消息相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1884136289955422210, 1843834117584949249, 'gen:template:', '代码生成模板相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1884136159525150722, 1843834040632053762, 'gen:database:', '代码生成数据源相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1884136508323471361, 1843834040632053762, 'gen:table:', '预览下载相关接口');
INSERT INTO "public"."sys_resource_api" VALUES (1884135290880598018, 1705405650779348993, 'sys:region:', '行政区域相关接口');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "alias" varchar(30) COLLATE "pg_catalog"."default",
    "remark" varchar(255) COLLATE "pg_catalog"."default",
    "status" int2 NOT NULL DEFAULT 1,
    "sort" int4 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."sys_role"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_role"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_role"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_role"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_role"."alias" IS '别名';
COMMENT ON COLUMN "public"."sys_role"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_role"."status" IS '状态 0、禁用 1、正常';
COMMENT ON COLUMN "public"."sys_role"."sort" IS '排序';
COMMENT ON TABLE "public"."sys_role" IS '系统角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES (1778231636381663234, 0, 'admin', '2024-04-11 09:20:34.717', 'admin', '2025-06-02 13:56:52.47', 0, 'CEO', 'ceo', NULL, 1, 7);
INSERT INTO "public"."sys_role" VALUES (0, 0, 'admin', '2021-11-04 12:16:50', 'admin', '2025-06-02 13:57:01.066', 0, '系统管理员', 'systemAdmin', '管理角色', 1, 6);
INSERT INTO "public"."sys_role" VALUES (1456247408778260482, 0, 'admin', '2021-11-04 21:10:00', 'admin', '2025-06-02 13:57:08.47', 0, '技术总监', 'technicalDirector', '技术总监', 1, 5);
INSERT INTO "public"."sys_role" VALUES (1492884198322536450, 0, 'admin', '2022-02-13 23:31:00', 'admin', '2025-06-02 13:57:15.475', 0, '财务总监', 'financialOfficer', '财务总监', 1, 3);
INSERT INTO "public"."sys_role" VALUES (1778264458643374082, 0, 'admin', '2024-04-11 11:31:00.153', 'admin', '2025-06-02 13:57:23.607', 0, '开发人员', 'developer', NULL, 1, 1);
INSERT INTO "public"."sys_role" VALUES (1778246292458438657, 0, 'admin', '2024-04-11 10:18:48.998', 'admin', '2025-06-02 13:57:31.741', 0, '考勤管理员', '考勤管理员', NULL, 1, 1);
INSERT INTO "public"."sys_role" VALUES (1696505296993959937, 0, 'admin', '2023-08-29 20:49:17.004', 'admin', '2025-06-02 13:57:39.573', 0, '测试人员', 'testingPersonnel', '测试人员', 0, 1);
INSERT INTO "public"."sys_role" VALUES (1778266027724111874, 0, 'admin', '2024-04-11 11:37:14.252', 'admin', '2025-06-02 13:58:20.214', 0, '部门领导', '部门领导', NULL, 1, 1);
INSERT INTO "public"."sys_role" VALUES (1, 0, '0', '2021-11-04 20:15:35', 'admin', '2025-06-02 13:58:28.037', 0, '普通员工', 'employees', '普通员工', 1, 1);

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_resource";
CREATE TABLE "public"."sys_role_resource" (
    "id" int8 NOT NULL,
    "role_id" int8 NOT NULL,
    "resource_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_role_resource"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_role_resource"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_resource"."resource_id" IS '资源ID';
COMMENT ON TABLE "public"."sys_role_resource" IS '系统角色资源';

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO "public"."sys_role_resource" VALUES (1929417073236828162, 1778246292458438657, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929417073236828163, 1778246292458438657, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929417073236828164, 1778246292458438657, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929417073236828165, 1778246292458438657, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929417073274576898, 1778246292458438657, 1824089134229397505);
INSERT INTO "public"."sys_role_resource" VALUES (1929417073274576899, 1778246292458438657, 1818217918824460289);
INSERT INTO "public"."sys_role_resource" VALUES (1929417073274576900, 1778246292458438657, 1818497082743914497);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062658, 1696505296993959937, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062659, 1696505296993959937, 1705397288498995201);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062660, 1696505296993959937, 1705403264375562242);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062661, 1696505296993959937, 1705403612024643585);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062662, 1696505296993959937, 1705405860804927489);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062663, 1696505296993959937, 1705404432669581313);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062664, 1696505296993959937, 1705404534842826753);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062665, 1696505296993959937, 1705405518658772993);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062666, 1696505296993959937, 1705405971014459394);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062667, 1696505296993959937, 1705409192441257985);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062668, 1696505296993959937, 1705405650779348993);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106053062669, 1696505296993959937, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106124365825, 1696505296993959937, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929417106124365826, 1696505296993959937, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929417276526354434, 1778266027724111874, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929417276526354435, 1778266027724111874, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929417276526354436, 1778266027724111874, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929417276526354437, 1778266027724111874, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929417309355171841, 1, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929417309355171842, 1, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929417309355171843, 1, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929417309355171844, 1, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908530704385, 1778231636381663234, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908530704386, 1778231636381663234, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908530704387, 1778231636381663234, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908530704388, 1778231636381663234, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908530704389, 1778231636381663234, 1705397288498995201);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908530704390, 1778231636381663234, 1705403264375562242);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908530704391, 1778231636381663234, 1705403612024643585);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908530704392, 1778231636381663234, 1705405860804927489);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647425, 1778231636381663234, 1705404432669581313);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647426, 1778231636381663234, 1705404534842826753);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647427, 1778231636381663234, 1705405518658772993);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647428, 1778231636381663234, 1705405971014459394);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647429, 1778231636381663234, 1705409192441257985);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647430, 1778231636381663234, 1705405650779348993);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647431, 1778231636381663234, 1843833708455759874);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647432, 1778231636381663234, 1843834040632053762);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647433, 1778231636381663234, 1843834117584949249);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647434, 1778231636381663234, 1818497082743914497);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647435, 1778231636381663234, 1818217918824460289);
INSERT INTO "public"."sys_role_resource" VALUES (1929416908572647436, 1778231636381663234, 1824089134229397505);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552962, 0, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552963, 0, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552964, 0, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552965, 0, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552966, 0, 1705397288498995201);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552967, 0, 1705403264375562242);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552968, 0, 1705403612024643585);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552969, 0, 1705405860804927489);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552970, 0, 1705404432669581313);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552971, 0, 1705404534842826753);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552972, 0, 1705405518658772993);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552973, 0, 1705405971014459394);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552974, 0, 1705409192441257985);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552975, 0, 1705405650779348993);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552976, 0, 1843833708455759874);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552977, 0, 1843834040632053762);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552978, 0, 1843834117584949249);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552979, 0, 1818497082743914497);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552980, 0, 1818217918824460289);
INSERT INTO "public"."sys_role_resource" VALUES (1929416944576552981, 0, 1824089134229397505);
INSERT INTO "public"."sys_role_resource" VALUES (1929416975614402561, 1456247408778260482, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929416975614402562, 1456247408778260482, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929416975614402563, 1456247408778260482, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929416975614402564, 1456247408778260482, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929417005008084994, 1492884198322536450, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929417005008084995, 1492884198322536450, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929417005008084996, 1492884198322536450, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929417005008084997, 1492884198322536450, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929417039120359426, 1778264458643374082, 1818549132156563458);
INSERT INTO "public"."sys_role_resource" VALUES (1929417039120359427, 1778264458643374082, 1710476160198299650);
INSERT INTO "public"."sys_role_resource" VALUES (1929417039120359428, 1778264458643374082, 1710488066858471425);
INSERT INTO "public"."sys_role_resource" VALUES (1929417039120359429, 1778264458643374082, 1734132265541500930);
INSERT INTO "public"."sys_role_resource" VALUES (1929417039120359430, 1778264458643374082, 1818497082743914497);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
    "id" int8 NOT NULL,
    "create_id" int8 NOT NULL,
    "create_by" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) NOT NULL,
    "update_by" varchar(30) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "deleted" int2 NOT NULL DEFAULT 0,
    "username" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "password" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "salt" varchar(8) COLLATE "pg_catalog"."default" NOT NULL,
    "real_name" varchar(100) COLLATE "pg_catalog"."default",
    "nick_name" varchar(20) COLLATE "pg_catalog"."default",
    "avatar" varchar(200) COLLATE "pg_catalog"."default",
    "sex" char(1) COLLATE "pg_catalog"."default" NOT NULL,
    "phone" varchar(11) COLLATE "pg_catalog"."default",
    "phone_verified" int2 NOT NULL DEFAULT 0,
    "email" varchar(100) COLLATE "pg_catalog"."default",
    "email_verified" int2 NOT NULL DEFAULT 0,
    "status" int2 NOT NULL DEFAULT 1,
    "job_num" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_user"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."sys_user"."create_id" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '修改人';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sys_user"."deleted" IS '删除 0、否 1、是';
COMMENT ON COLUMN "public"."sys_user"."username" IS '账号';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."salt" IS '随机盐';
COMMENT ON COLUMN "public"."sys_user"."real_name" IS '真实名称';
COMMENT ON COLUMN "public"."sys_user"."nick_name" IS '昵称';
COMMENT ON COLUMN "public"."sys_user"."avatar" IS '头像';
COMMENT ON COLUMN "public"."sys_user"."sex" IS '性别';
COMMENT ON COLUMN "public"."sys_user"."phone" IS '手机号';
COMMENT ON COLUMN "public"."sys_user"."phone_verified" IS '手机号是否验证 0、否 1、是';
COMMENT ON COLUMN "public"."sys_user"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_user"."email_verified" IS '邮箱是否验证 0、否 1、是';
COMMENT ON COLUMN "public"."sys_user"."status" IS '状态 0、禁用 1、正常';
COMMENT ON COLUMN "public"."sys_user"."job_num" IS '工号';
COMMENT ON TABLE "public"."sys_user" IS '系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (1778236187025342466, 0, 'admin', '2024-04-11 09:38:39.675', 'admin', '2024-05-02 13:25:13.764', 0, 'test02', '9682d343fb1edddc09ecff80db8ee4f8', '9D9mzUHs', '夏小华', '部门领导2', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO "public"."sys_user" VALUES (1778236021094481921, 0, 'admin', '2024-04-11 09:38:00.114', 'admin', '2024-05-02 13:25:13.772', 0, 'test01', 'be03be15c4dc9fd4e03b6af45710a251', '1O283K29', '杨小凤', '部门领导1', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO "public"."sys_user" VALUES (1778235419958444034, 0, 'admin', '2024-04-11 09:35:36.791', 'admin', '2024-05-02 13:25:13.784', 0, 'test03', '46c4c7007ee6b3403d5726f371e694b4', '34981X24', '笪小琴', '人事主管', NULL, '女', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO "public"."sys_user" VALUES (1778235567803465730, 0, 'admin', '2024-04-11 09:36:12.041', 'admin', '2024-05-02 13:25:13.796', 0, 'test04', '8022258b8833dd871b73df3a84f32047', 't8lpphzD', '陈小超', '财务', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO "public"."sys_user" VALUES (1778264912529981441, 0, 'admin', '2024-04-11 11:32:48.368', 'admin', '2024-05-02 13:25:13.805', 0, 'test06', 'b0b63f9e96e37de8b31fb07168d84205', '44r6N95v', '陈小辉', '开发人员2', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO "public"."sys_user" VALUES (1778248547756670978, 0, 'admin', '2024-04-11 10:27:46.702', 'admin', '2024-05-02 13:25:13.813', 0, 'test05', '522663acf3ebf865be8ee302562f6200', '1L3n01Uh', '李小广', '开发人员1', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO "public"."sys_user" VALUES (1, 0, 'admin', '2024-06-10 17:14:08.627', 'admin', '2024-06-10 17:14:35.735', 0, 'aizuda', '22178093497d6dd251bf667f757a1a28', 'V6438t97', 'AIZUDA', NULL, NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO "public"."sys_user" VALUES (0, 0, '青苗', '2021-11-02 00:32:16', 'admin', '2024-05-02 13:25:13.756', 0, 'admin', '619798f74b24a7f75aad6032cb13ab32', '9262Q6l8', 'CEO', 'CEO', NULL, '男', NULL, 0, NULL, 0, 1, NULL);

-- ----------------------------
-- Table structure for sys_user_department
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_department";
CREATE TABLE "public"."sys_user_department" (
    "id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "department_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_department"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_department"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_department"."department_id" IS '部门ID';
COMMENT ON TABLE "public"."sys_user_department" IS '系统用户部门';

-- ----------------------------
-- Records of sys_user_department
-- ----------------------------
INSERT INTO "public"."sys_user_department" VALUES (1497974292503031810, 1, 1456247408778260482);
INSERT INTO "public"."sys_user_department" VALUES (1694693758976966658, 1694693758968578049, 1456247408778260482);
INSERT INTO "public"."sys_user_department" VALUES (1694693758976966659, 1694693758968578049, 1492884198322536450);
INSERT INTO "public"."sys_user_department" VALUES (1694693758981160961, 1694693758968578049, 1);
INSERT INTO "public"."sys_user_department" VALUES (1694693758981160962, 1694693758968578049, 0);
INSERT INTO "public"."sys_user_department" VALUES (1694694001726504962, 1694694001718116354, 0);
INSERT INTO "public"."sys_user_department" VALUES (1694753487552036866, 1694524980297252866, 1456247408778260482);
INSERT INTO "public"."sys_user_department" VALUES (1694754654189305858, 1694523894970761218, 1);
INSERT INTO "public"."sys_user_department" VALUES (1703707996831719425, 1694698980486987777, 0);
INSERT INTO "public"."sys_user_department" VALUES (1703707996831719426, 1694698980486987777, 1456247408778260482);
INSERT INTO "public"."sys_user_department" VALUES (1703707996831719427, 1694698980486987777, 1492884198322536450);
INSERT INTO "public"."sys_user_department" VALUES (1703707996831719428, 1694698980486987777, 1);
INSERT INTO "public"."sys_user_department" VALUES (1703707996835913730, 1694698980486987777, 1696505296993959937);
INSERT INTO "public"."sys_user_department" VALUES (1705068256192471041, 1705067852272607233, 0);
INSERT INTO "public"."sys_user_department" VALUES (1705068256196665345, 1705067852272607233, 1696505296993959937);
INSERT INTO "public"."sys_user_department" VALUES (1705068256196665346, 1705067852272607233, 1);
INSERT INTO "public"."sys_user_department" VALUES (1705068256196665347, 1705067852272607233, 1492884198322536450);
INSERT INTO "public"."sys_user_department" VALUES (1705068256196665348, 1705067852272607233, 1456247408778260482);
INSERT INTO "public"."sys_user_department" VALUES (1771165796622028802, 1694523277028147201, 1740953843466010625);
INSERT INTO "public"."sys_user_department" VALUES (1776867375126446082, 1773278598010699778, 3);
INSERT INTO "public"."sys_user_department" VALUES (1778234707618824193, 1749994976754032641, 1696349127893630978);
INSERT INTO "public"."sys_user_department" VALUES (1778234753261240321, 1749994885959933953, 3);
INSERT INTO "public"."sys_user_department" VALUES (1778238506618060801, 0, 3);
INSERT INTO "public"."sys_user_department" VALUES (1778258222266974210, 1778236187025342466, 2);
INSERT INTO "public"."sys_user_department" VALUES (1778258250159095810, 1778236021094481921, 2);
INSERT INTO "public"."sys_user_department" VALUES (1778258364286107649, 1778235419958444034, 1778247724637093889);
INSERT INTO "public"."sys_user_department" VALUES (1778258417193058305, 1778235567803465730, 1778237792399392769);
INSERT INTO "public"."sys_user_department" VALUES (1778264717008306177, 1778248547756670978, 2);
INSERT INTO "public"."sys_user_department" VALUES (1778264912542564353, 1778264912529981441, 1778247413147107329);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role" (
    "id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_role"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_role"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."sys_user_role" IS '系统用户角色';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "public"."sys_user_role" VALUES (1694693758976966658, 1694693758968578049, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1694693758976966659, 1694693758968578049, 1492884198322536450);
INSERT INTO "public"."sys_user_role" VALUES (1694693758981160961, 1694693758968578049, 1);
INSERT INTO "public"."sys_user_role" VALUES (1694693758981160962, 1694693758968578049, 0);
INSERT INTO "public"."sys_user_role" VALUES (1694694001726504962, 1694694001718116354, 0);
INSERT INTO "public"."sys_user_role" VALUES (1694753487552036866, 1694524980297252866, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1694754654189305858, 1694523894970761218, 1);
INSERT INTO "public"."sys_user_role" VALUES (1703707996831719425, 1694698980486987777, 0);
INSERT INTO "public"."sys_user_role" VALUES (1703707996831719426, 1694698980486987777, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1703707996831719427, 1694698980486987777, 1492884198322536450);
INSERT INTO "public"."sys_user_role" VALUES (1703707996831719428, 1694698980486987777, 1);
INSERT INTO "public"."sys_user_role" VALUES (1703707996835913730, 1694698980486987777, 1696505296993959937);
INSERT INTO "public"."sys_user_role" VALUES (1705068256192471041, 1705067852272607233, 0);
INSERT INTO "public"."sys_user_role" VALUES (1705068256196665345, 1705067852272607233, 1696505296993959937);
INSERT INTO "public"."sys_user_role" VALUES (1705068256196665346, 1705067852272607233, 1);
INSERT INTO "public"."sys_user_role" VALUES (1705068256196665347, 1705067852272607233, 1492884198322536450);
INSERT INTO "public"."sys_user_role" VALUES (1705068256196665348, 1705067852272607233, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1711627814540976129, 1694320017608589314, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1711627814540976130, 1694320017608589314, 1492884198322536450);
INSERT INTO "public"."sys_user_role" VALUES (1711627814540976131, 1694320017608589314, 1);
INSERT INTO "public"."sys_user_role" VALUES (1764821434647240705, 1711682128658026497, 1);
INSERT INTO "public"."sys_user_role" VALUES (1764821434659823617, 1711682128658026497, 1696505296993959937);
INSERT INTO "public"."sys_user_role" VALUES (1764821434659823618, 1711682128658026497, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1771165796622028801, 1694523277028147201, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1776867375105474561, 1773278598010699778, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1778234707597852673, 1749994976754032641, 1696505296993959937);
INSERT INTO "public"."sys_user_role" VALUES (1778234753248657410, 1749994885959933953, 0);
INSERT INTO "public"."sys_user_role" VALUES (1778258364273524737, 1778235419958444034, 1778246292458438657);
INSERT INTO "public"."sys_user_role" VALUES (1778258417180475394, 1778235567803465730, 1492884198322536450);
INSERT INTO "public"."sys_user_role" VALUES (1778264716999917569, 1778248547756670978, 1778264458643374082);
INSERT INTO "public"."sys_user_role" VALUES (1778264912538370050, 1778264912529981441, 1778264458643374082);
INSERT INTO "public"."sys_user_role" VALUES (1778266180442914818, 1778236021094481921, 1778266027724111874);
INSERT INTO "public"."sys_user_role" VALUES (1800094198569537538, 1800094084849373185, 0);
INSERT INTO "public"."sys_user_role" VALUES (1815367038679453697, 0, 1778231636381663234);
INSERT INTO "public"."sys_user_role" VALUES (1815367038679453698, 0, 1456247408778260482);
INSERT INTO "public"."sys_user_role" VALUES (1815367038679453699, 0, 1778266027724111874);
INSERT INTO "public"."sys_user_role" VALUES (1815367038679453700, 0, 1778264458643374082);
INSERT INTO "public"."sys_user_role" VALUES (1815367038679453701, 0, 0);
INSERT INTO "public"."sys_user_role" VALUES (1815367090382639105, 1778236187025342466, 1778266027724111874);
INSERT INTO "public"."sys_user_role" VALUES (1815367090382639106, 1778236187025342466, 1492884198322536450);
INSERT INTO "public"."sys_user_role" VALUES (1497974292503031810, 1, 0);

-- ----------------------------
-- Primary Key structure for table gen_database
-- ----------------------------
ALTER TABLE "public"."gen_database" ADD CONSTRAINT "gen_database_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table gen_template
-- ----------------------------
ALTER TABLE "public"."gen_template" ADD CONSTRAINT "gen_template_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_app
-- ----------------------------
ALTER TABLE "public"."sys_app" ADD CONSTRAINT "sys_app_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_configure
-- ----------------------------
ALTER TABLE "public"."sys_configure" ADD CONSTRAINT "sys_configure_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_department
-- ----------------------------
ALTER TABLE "public"."sys_department" ADD CONSTRAINT "sys_user_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_message
-- ----------------------------
ALTER TABLE "public"."sys_message" ADD CONSTRAINT "sys_message_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_message_receiver
-- ----------------------------
ALTER TABLE "public"."sys_message_receiver" ADD CONSTRAINT "sys_message_receiver_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_post
-- ----------------------------
ALTER TABLE "public"."sys_post" ADD CONSTRAINT "sys_post_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_region
-- ----------------------------
ALTER TABLE "public"."sys_region" ADD CONSTRAINT "sys_region_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_resource
-- ----------------------------
ALTER TABLE "public"."sys_resource" ADD CONSTRAINT "rbac_role_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_resource_api
-- ----------------------------
ALTER TABLE "public"."sys_resource_api" ADD CONSTRAINT "sys_resource_api_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_role_resource
-- ----------------------------
ALTER TABLE "public"."sys_role_resource" ADD CONSTRAINT "rbac_user_role_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "azd_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_department
-- ----------------------------
ALTER TABLE "public"."sys_user_department" ADD CONSTRAINT "sys_user_role_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role" ADD CONSTRAINT "rbac_user_role_pkey" PRIMARY KEY ("id");
