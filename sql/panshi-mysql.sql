
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_database
-- ----------------------------
DROP TABLE IF EXISTS `gen_database`;
CREATE TABLE `gen_database`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
    `alias` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '别名',
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
    `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `port` int NOT NULL COMMENT '端口',
    `host` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主机',
    `db_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库名',
    `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成数据源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_database
-- ----------------------------
INSERT INTO `gen_database` VALUES (1829328106278948865, 0, 'admin', '2024-08-30 09:19:42', NULL, NULL, 0, 'pgsql', 'aizuda', 'postgres', '123456', 5432, '127.0.0.1', 'aizuda-boot', NULL);

-- ----------------------------
-- Table structure for gen_template
-- ----------------------------
DROP TABLE IF EXISTS `gen_template`;
CREATE TABLE `gen_template`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `tpl_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
    `tpl_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板内容',
    `out_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '输出文件',
    `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板描述',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_template
-- ----------------------------
INSERT INTO `gen_template` VALUES (1829399584248733698, 0, 'admin', '2024-08-30 14:03:44', 'admin', '2024-10-13 16:43:28', 0, '测试模板', '', 'mapper/%sMapper.xml', '123');
INSERT INTO `gen_template` VALUES (1839920582442209282, 0, 'admin', '2024-09-28 14:50:26', 'admin', '2024-10-13 15:51:32', 0, '实体', 'package ${package.Entity};\r\n\r\n#foreach($pkg in ${table.importPackages})\r\nimport ${pkg};\r\n#end\r\n#if(${entityLombokModel})\r\n\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\nimport lombok.Setter;\r\nimport lombok.Getter;\r\n#end\r\n\r\n/**\r\n * $!{table.comment}\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n#if(${entityLombokModel})\r\n@Getter\r\n@Setter\r\n@Schema(name = \"${entity}\", description = \"$!{table.comment}\")\r\n#end\r\n#if(${table.convert})\r\n@TableName(\"${table.name}\")\r\n#end\r\n#if(${superEntityClass})\r\npublic class ${entity} extends ${superEntityClass} {\r\n#else\r\npublic class ${entity} implements Serializable {\r\n#end\r\n#foreach($field in ${table.fields})\r\n#if(${field.keyFlag})\r\n#set($keyPropertyName=${field.propertyName})\r\n#end\r\n#if(\"$!field.comment\" != \"\")\r\n\r\n	@Schema(description = \"${field.comment}\")\r\n#end\r\n#if(${field.keyFlag})\r\n#if(${field.keyIdentityFlag})\r\n	@TableId(value=\"${field.name}\", type= IdType.AUTO)\r\n#elseif(${field.convert})\r\n    @TableId(\"${field.name}\")\r\n#end\r\n#elseif(${field.fill})\r\n#if(${field.convert})\r\n	@TableField(value = \"${field.name}\", fill = FieldFill.${field.fill})\r\n#else\r\n	@TableField(fill = FieldFill.${field.fill})\r\n#end\r\n#elseif(${field.convert})\r\n	@TableField(\"${field.name}\")\r\n#end\r\n#if(${logicDeleteFieldName}==${field.name})\r\n	@TableLogic\r\n#end\r\n${field.customMap.validationAnnotations}private ${field.propertyType} ${field.propertyName};\r\n#end\r\n\r\n#if(!${entityLombokModel})\r\n#foreach($field in ${table.fields})\r\n#if(${field.propertyType.equals(\"Boolean\")})\r\n#set($getprefix=\"is\")\r\n#else\r\n#set($getprefix=\"get\")\r\n#end\r\n\r\n	public ${field.propertyType} ${getprefix}${field.capitalName}() {\r\n		return ${field.propertyName};\r\n	}\r\n\r\n#if(${entityBuilderModel})\r\n	public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n#else\r\n	public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n#end\r\n		this.${field.propertyName} = ${field.propertyName};\r\n#if(${entityBuilderModel})\r\n		return this;\r\n#end\r\n	}\r\n#end\r\n#end\r\n}\r\n', '%s.java', '实体模板');

-- ----------------------------
-- Table structure for sys_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `identification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标识',
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密钥',
    `status` smallint NOT NULL DEFAULT 1 COMMENT '状态 0、禁用 1、正常',
    `expire` datetime NOT NULL COMMENT '授权到期',
    `sort` smallint NOT NULL DEFAULT 0 COMMENT '排序',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_app
-- ----------------------------
INSERT INTO `sys_app` VALUES (1682692363772092417, 0, 'admin', '2023-07-22 18:01:00', 'admin', '2023-12-30 12:42:15', 0, 'ToTeet', 'toTeet', '122121', 1, '2023-07-25 18:01:23', 1);
INSERT INTO `sys_app` VALUES (1688820205836636161, 0, 'admin', '2023-08-08 15:51:28', 'admin', '2023-12-30 12:42:20', 0, 'hiAzd', 'hiAzd', '123123', 0, '2023-08-09 15:51:22', 1);

-- ----------------------------
-- Table structure for sys_configure
-- ----------------------------
DROP TABLE IF EXISTS `sys_configure`;
CREATE TABLE `sys_configure`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类',
    `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关键字',
    `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
    `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
    `sort` smallint NOT NULL DEFAULT 0 COMMENT '排序',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '扩展配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_configure
-- ----------------------------
INSERT INTO `sys_configure` VALUES (1495062995721515010, 0, 'admin', '2022-02-19 23:49:00', 'admin', '2023-12-30 12:44:13', 0, '系统参数', 'msgConfig', 'mobile=1', '短信配置', 2);
INSERT INTO `sys_configure` VALUES (1495063397821050881, 0, 'admin', '2022-02-19 23:51:00', 'admin', '2023-08-29 21:03:35', 0, '业务参数', 'codeSwitch', 'off', '发送验证码开关', 6);

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `pid` bigint NOT NULL DEFAULT 0 COMMENT '父ID',
    `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `sort` smallint NOT NULL DEFAULT 0 COMMENT '排序',
    `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `status` smallint NOT NULL DEFAULT 1 COMMENT '状态 0、禁用 1、正常',
    `head_id` bigint NULL DEFAULT NULL COMMENT '主管ID',
    `head_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主管名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO `sys_department` VALUES (1, 0, 'admin', '2022-02-20 22:19:00', 'admin', '2023-12-30 14:23:47', 0, 2, '开发 A 组', '2020', 1, '开发 A 组', 1, NULL, NULL);
INSERT INTO `sys_department` VALUES (2, 0, 'admin', '2021-11-07 10:13:54', 'admin', '2024-04-11 10:22:47', 0, 3, '产品部', '2000', 0, '产品部', 1, 1778236021094481921, '杨小凤');
INSERT INTO `sys_department` VALUES (3, 0, 'admin', '2022-02-20 22:21:20', 'admin', '2024-05-02 13:18:54', 0, 0, '飞龙驱动科技', '1000', 2, '太阳科技', 1, 0, 'CEO');
INSERT INTO `sys_department` VALUES (1696346580223049729, 0, 'admin', '2023-08-29 10:18:36', 'admin', '2023-12-30 14:23:52', 0, 2, '开发 B 组', '2010', 1, '开发 B 组', 1, NULL, NULL);
INSERT INTO `sys_department` VALUES (1778237792399392769, 0, 'admin', '2024-04-11 09:45:02', NULL, NULL, 0, 3, '财务部', '0023', 4, '财务部', 0, 1778236187025342466, NULL);
INSERT INTO `sys_department` VALUES (1778246587070545922, 0, 'admin', '2024-04-11 10:19:59', NULL, NULL, 0, 3, '综合管理部', 'zongheguanli', 1, '综合管理', 1, 1778236187025342466, '部门领导');
INSERT INTO `sys_department` VALUES (1778247413147107329, 0, 'admin', '2024-04-11 10:23:16', NULL, NULL, 0, 3, '应用部', 'yingyongbu', 2, '应用部', 1, 1778236187025342466, '夏小华');
INSERT INTO `sys_department` VALUES (1778247724637093889, 0, 'admin', '2024-04-11 10:24:30', NULL, NULL, 0, 1778246587070545922, '人事部', 'renshibu', 1, '人事部', 1, 1778235419958444034, '出纳');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `pid` bigint NOT NULL DEFAULT 0 COMMENT '父ID',
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
    `status` smallint NOT NULL DEFAULT 1 COMMENT '状态 0、禁用 1、正常',
    `sort` smallint NOT NULL DEFAULT 0 COMMENT '排序',
    `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1479120984875122689, 0, 'admin', '2022-01-07 00:01:38', 'admin', '2023-07-21 15:23:58', 0, 0, '会员类型', 'memberType', NULL, 1, 13, '会员类型');
INSERT INTO `sys_dict` VALUES (1682657662327971842, 0, 'admin', '2023-07-22 15:43:44', NULL, NULL, 0, 0, '测试字典', 'testDic', NULL, 1, 12, '');
INSERT INTO `sys_dict` VALUES (1479121109454340098, 0, 'admin', '2022-01-07 00:02:08', 'admin', '2023-12-30 18:25:41', 0, 0, '消息类型', 'msgType', NULL, 1, 110, '消息类型');
INSERT INTO `sys_dict` VALUES (1480140709017047042, 0, 'admin', '2022-01-09 19:33:00', 'admin', '2023-12-30 18:23:29', 0, 1479120984875122689, '普通会员', '100', '1', 1, 1, '普通会员');
INSERT INTO `sys_dict` VALUES (1741777618865938433, 0, 'admin', '2024-01-01 19:05:20', NULL, NULL, 0, 1479120984875122689, '黄金会员', '102', '2', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (1741778332312854529, 0, 'admin', '2024-01-01 19:08:10', NULL, NULL, 0, 1479120984875122689, '铂金会员', '101', '3', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (1688816911709966337, 0, 'admin', '2023-08-08 15:38:00', 'admin', '2023-12-30 18:26:50', 0, 1479121109454340098, '系统', '100', '1', 1, 1, '系统消息');
INSERT INTO `sys_dict` VALUES (1481293017771884546, 0, 'admin', '2022-01-12 23:52:31', 'admin', '2023-12-30 18:27:14', 0, 1479121109454340098, '邮箱', 'email', '2', 1, 2, '发送邮件');
INSERT INTO `sys_dict` VALUES (1481293159988150273, 0, 'admin', '2022-01-12 23:53:00', 'admin', '2023-12-30 18:27:24', 0, 1479121109454340098, '短信', '101', '3', 1, 2, '发送短信');
INSERT INTO `sys_dict` VALUES (1696509959562641410, 0, 'admin', '2023-08-29 21:07:49', 'admin', '2023-12-30 18:29:16', 0, 1682657662327971842, 'test', '11', '1', 1, 11, NULL);
INSERT INTO `sys_dict` VALUES (1696509851961966594, 0, 'admin', '2023-08-29 21:07:23', 'admin', '2023-12-30 18:28:12', 0, 1682657662327971842, 'key1', '33', '2', 0, 3, NULL);

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
    `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
    `category` smallint NOT NULL COMMENT '类别 0，通知 1，消息 2，待办',
    `business_id` bigint NULL DEFAULT NULL COMMENT '业务ID',
    `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业务类型',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_message
-- ----------------------------
INSERT INTO `sys_message` VALUES (1709154417339256833, 0, 'admin', '2023-10-03 18:32:22', 'admin', '2023-12-27 20:18:52', '通知 6', '通知 6 内容', 0, NULL, NULL);
INSERT INTO `sys_message` VALUES (1709154710567243777, 0, 'admin', '2023-10-03 18:33:32', 'admin', '2023-10-12 08:35:34', '待办 2', '待办 2 内容', 2, NULL, NULL);
INSERT INTO `sys_message` VALUES (1709154768926789634, 0, 'admin', '2023-10-03 18:33:46', 'admin', '2023-12-21 20:50:22', '待办 5', '待办 5 内容', 2, NULL, NULL);
INSERT INTO `sys_message` VALUES (1835629624119488513, 0, 'admin', '2024-09-16 18:39:41', NULL, NULL, '流程：测试业务流程（采购单） 待审批', '流程：测试业务流程（采购单） 待审批 ，当前所在节点：审核人 ，任务发起人：admin', 2, 1835629624023019522, 'flowTask');
INSERT INTO `sys_message` VALUES (1844375699767443457, 0, 'admin', '2024-10-10 21:53:28', NULL, NULL, '流程：自选审批 待审批', '流程：自选审批 待审批 ，当前所在节点：审核人 ，任务发起人：admin', 2, 1844375699075383297, 'flowTodoTask');
INSERT INTO `sys_message` VALUES (1844385185366396929, 1778248547756670978, 'test05', '2024-10-10 22:31:10', NULL, NULL, '流程：自选审批 待审批', '流程：自选审批 待审批 ，当前所在节点：审核人 ，任务发起人：test05', 2, 1844375699075383297, 'flowTodoTask');

-- ----------------------------
-- Table structure for sys_message_receiver
-- ----------------------------
DROP TABLE IF EXISTS `sys_message_receiver`;
CREATE TABLE `sys_message_receiver`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `message_id` bigint NOT NULL COMMENT '消息ID',
    `user_id` bigint NOT NULL COMMENT '接收人ID',
    `viewed` smallint NOT NULL DEFAULT 0 COMMENT '已查看 0，否 1，是',
    `send_status` smallint NOT NULL DEFAULT 0 COMMENT '发送状态 0，未发送 1，成功 2，失败',
    `send_failure` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发送失败原因',
    `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息接收人表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_message_receiver
-- ----------------------------
INSERT INTO `sys_message_receiver` VALUES (1709154417339256833, 1709154417339256833, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_message_receiver` VALUES (1709154710567243777, 1709154710567243777, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_message_receiver` VALUES (1709154768926789634, 1709154768926789634, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_message_receiver` VALUES (1835629019904819202, 1835629019837710338, 0, 1, 1, NULL, '2024-09-16 18:37:17');
INSERT INTO `sys_message_receiver` VALUES (1835629624169820162, 1835629624119488513, 0, 1, 1, NULL, '2024-09-16 18:39:41');
INSERT INTO `sys_message_receiver` VALUES (1844375699863912449, 1844375699767443457, 1778248547756670978, 0, 2, '未上线', '2024-10-10 21:53:28');
INSERT INTO `sys_message_receiver` VALUES (1844385185366396930, 1844385185366396929, 1778264912529981441, 0, 2, '未上线', '2024-10-10 22:31:10');
INSERT INTO `sys_message_receiver` VALUES (1844720796367376386, 1844720796300267521, 0, 1, 1, NULL, '2024-10-11 20:44:46');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `sort` smallint NOT NULL DEFAULT 0 COMMENT '排序',
    `status` smallint NOT NULL DEFAULT 1 COMMENT '状态 0、禁用 1、正常',
    `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1494699308443709442, 0, 'admin', '2022-02-18 23:44:00', 'admin', '2024-04-12 20:43:57', 0, '研发人员', '3000', 11, 1, '研发岗位');
INSERT INTO `sys_post` VALUES (1494699370099978242, 0, 'admin', '2022-02-18 23:44:00', 'admin', '2023-12-30 12:22:46', 0, '测试人员', '3100', 3, 1, '测试岗位');
INSERT INTO `sys_post` VALUES (1494699404912701442, 0, 'admin', '2022-02-18 23:44:43', 'admin', '2023-12-30 12:19:47', 0, '财务经理', '1100', 1, 1, '公司财务负责人');
INSERT INTO `sys_post` VALUES (1695006999871537153, 0, 'admin', '2023-08-25 17:35:35', 'admin', '2023-12-30 12:21:55', 0, '董事高管', '1000', 5, 1, '公司管理层');
INSERT INTO `sys_post` VALUES (1740951042186838018, 0, 'admin', '2023-12-30 12:20:48', 'admin', '2024-08-22 22:18:46', 0, '人力经理', '2000', 3, 1, '人力资源负责人');
INSERT INTO `sys_post` VALUES (1740951504629825537, 0, 'admin', '2023-12-30 12:22:39', 'admin', '2024-04-15 20:37:13', 0, '部门负责人', '1300', 2, 0, '普通管理层');

-- ----------------------------
-- Table structure for sys_region
-- ----------------------------
DROP TABLE IF EXISTS `sys_region`;
CREATE TABLE `sys_region`  (
    `id` bigint NOT NULL COMMENT '类型 0，省份直辖市 1，地市 2，区县0',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `pid` bigint NOT NULL DEFAULT 0 COMMENT '父ID',
    `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `sort` smallint NOT NULL DEFAULT 0 COMMENT '排序',
    `level` smallint NOT NULL COMMENT '类型 0，省份直辖市 1，地市 2，区县',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '行政区域' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_region
-- ----------------------------
INSERT INTO `sys_region` VALUES (1496872692367691778, 0, 'admin', '2022-02-24 23:40:00', 'admin', '2023-07-27 10:29:55', 0, 1496875814116872194, '北京市', '1100', 1100, 2);
INSERT INTO `sys_region` VALUES (1496875814116872194, 0, 'admin', '2022-02-24 23:52:59', NULL, NULL, 0, 1496872692367691778, '北京市', '110100', 1101, 1);
INSERT INTO `sys_region` VALUES (1496875919859470338, 0, 'admin', '2022-02-24 23:53:25', NULL, NULL, 0, 1496875814116872194, '东城区', '110101', 1101, 2);
INSERT INTO `sys_region` VALUES (1496889386024792065, 0, 'admin', '2022-02-25 00:46:55', NULL, NULL, 0, 1496875814116872194, '西城区', '110102', 110, 2);
INSERT INTO `sys_region` VALUES (1686549235646140418, 0, 'admin', '2023-08-02 09:27:27', 'admin', '2023-08-02 09:27:54', 0, 1686549235646140418, '测试 2', '123', 1, 2);
INSERT INTO `sys_region` VALUES (1686636956955774977, 0, 'admin', '2023-08-02 15:16:01', 'admin', '2023-08-02 15:24:52', 0, 1686636956955774977, '区域1', ' 1', 1, 1);
INSERT INTO `sys_region` VALUES (1686637003596435458, 0, 'admin', '2023-08-02 15:16:13', 'admin', '2023-08-02 15:20:45', 0, 1686637003596435458, '区域 1-1', '3', 1, 2);
INSERT INTO `sys_region` VALUES (1686638313905393666, 0, 'admin', '2023-08-02 15:21:25', 'admin', '2023-08-02 15:21:42', 0, 1686638313905393666, '区域 1-1', '1', 1, 2);
INSERT INTO `sys_region` VALUES (1686639222450360322, 0, 'admin', '2023-08-02 15:25:00', 'admin', '2023-12-30 15:02:26', 0, 0, '北京市', '1000', 1, 0);
INSERT INTO `sys_region` VALUES (1686639321326882818, 0, 'admin', '2023-08-02 15:25:25', 'admin', '2023-12-30 15:02:56', 0, 1686639222450360322, '海淀区', '1001', 3, 1);
INSERT INTO `sys_region` VALUES (1686657764474294273, 0, 'admin', '2023-08-02 15:25:00', 'admin', '2023-12-30 15:03:05', 0, 1686639222450360322, '东城区', '1002', 1, 1);
INSERT INTO `sys_region` VALUES (1740999425233948674, 0, 'admin', '2023-12-30 15:33:04', 'admin', '2023-12-30 15:33:29', 0, 0, '山东省', '2000', 1, 0);
INSERT INTO `sys_region` VALUES (1740999601650569217, 0, 'admin', '2023-12-30 15:33:46', NULL, NULL, 0, 1740999425233948674, '济南市', '2001', 1, 1);
INSERT INTO `sys_region` VALUES (1740999655740313601, 0, 'admin', '2023-12-30 15:33:59', NULL, NULL, 0, 1740999425233948674, '青岛市', '2002', 1, 1);
INSERT INTO `sys_region` VALUES (1740999720475201538, 0, 'admin', '2023-12-30 15:34:14', NULL, NULL, 0, 1740999425233948674, '烟台市', '2003', 1, 1);
INSERT INTO `sys_region` VALUES (1740999789739937794, 0, 'admin', '2023-12-30 15:34:31', NULL, NULL, 0, 1686639222450360322, '朝阳区', '1003', 1, 1);
INSERT INTO `sys_region` VALUES (1740999940604858369, 0, 'admin', '2023-12-30 15:35:07', NULL, NULL, 0, 0, '湖北省', '3000', 1, 0);
INSERT INTO `sys_region` VALUES (1740999994380029954, 0, 'admin', '2023-12-30 15:35:20', NULL, NULL, 0, 1740999940604858369, '武汉市', '3001', 1, 1);
INSERT INTO `sys_region` VALUES (1741000516059172866, 0, 'admin', '2023-12-30 15:37:24', NULL, NULL, 0, 1740999940604858369, '黄石市', '3002', 1, 1);
INSERT INTO `sys_region` VALUES (1741000624054112257, 0, 'admin', '2023-12-30 15:37:50', NULL, NULL, 0, 1740999940604858369, '襄阳市', '3003', 1, 1);
INSERT INTO `sys_region` VALUES (1741000786356899841, 0, 'admin', '2023-12-30 15:38:28', NULL, NULL, 0, 0, '河南省', '4000', 1, 0);
INSERT INTO `sys_region` VALUES (1741000913591111681, 0, 'admin', '2023-12-30 15:38:59', NULL, NULL, 0, 1741000786356899841, '郑州市', '4001', 1, 1);
INSERT INTO `sys_region` VALUES (1741000974114918402, 0, 'admin', '2023-12-30 15:39:13', NULL, NULL, 0, 1741000786356899841, '开封市', '4002', 1, 1);
INSERT INTO `sys_region` VALUES (1741001078536310786, 0, 'admin', '2023-12-30 15:39:38', NULL, NULL, 0, 1741000786356899841, '洛阳市', '3003', 1, 1);
INSERT INTO `sys_region` VALUES (1741001213450293249, 0, 'admin', '2023-12-30 15:40:10', NULL, NULL, 0, 0, '江西省', '5000', 1, 0);
INSERT INTO `sys_region` VALUES (1741001313794822146, 0, 'admin', '2023-12-30 15:40:34', NULL, NULL, 0, 1741001213450293249, '南昌市', '5001', 1, 1);
INSERT INTO `sys_region` VALUES (1741001430413250562, 0, 'admin', '2023-12-30 15:41:02', 'admin', '2023-12-30 15:41:32', 0, 1741001213450293249, '赣州市', '5002', 1, 1);
INSERT INTO `sys_region` VALUES (1741001528815816705, 0, 'admin', '2023-12-30 15:41:25', NULL, NULL, 0, 1741001213450293249, '抚州市', '5003', 1, 1);
INSERT INTO `sys_region` VALUES (1741001669568270338, 0, 'admin', '2023-12-30 15:41:59', NULL, NULL, 0, 0, '湖南省', '6000', 1, 0);
INSERT INTO `sys_region` VALUES (1741001777865199617, 0, 'admin', '2023-12-30 15:42:25', NULL, NULL, 0, 1741001669568270338, '长沙市', '6001', 1, 1);
INSERT INTO `sys_region` VALUES (1741001843342479362, 0, 'admin', '2023-12-30 15:42:40', NULL, NULL, 0, 1741001669568270338, '株洲市', '6002', 1, 1);
INSERT INTO `sys_region` VALUES (1741001905950855169, 0, 'admin', '2023-12-30 15:42:55', NULL, NULL, 0, 1741001669568270338, '湘潭市', '6003', 1, 1);
INSERT INTO `sys_region` VALUES (1763042934416281601, 0, 'admin', '2024-02-29 11:26:06', NULL, NULL, 0, 1741000516059172866, '鼎湖山', '3002001', 1, 2);

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL COMMENT '删除 0、否 1、是',
    `pid` bigint NOT NULL COMMENT '父ID',
    `title` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
    `alias` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '别名',
    `type` smallint NOT NULL COMMENT '类型 0，菜单 1，iframe 2，外链 3，按钮',
    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编码',
    `redirect` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重定向',
    `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件路径',
    `icon` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
    `status` smallint NOT NULL COMMENT '状态 0、禁用 1、正常',
    `sort` int NOT NULL COMMENT '排序',
    `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视图',
    `color` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '颜色',
    `hidden` tinyint(1) NOT NULL COMMENT '隐藏菜单',
    `parent_route` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级路由',
    `keep_alive` tinyint(1) NOT NULL COMMENT '保留查询参数',
    `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '查询携带参数',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES (1705397288498995201, 0, 'admin', '2023-09-23 09:42:00', 'admin', '2024-07-31 11:24:42', 0, 0, '系统设置', 'setting', 0, NULL, NULL, '/setting', 'icon-swagger', 1, 10, 'layout.base', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705403264375562242, 0, 'admin', '2023-09-23 10:06:00', 'admin', '2024-08-10 18:38:37', 0, 1705397288498995201, '用户管理', 'user', 0, NULL, NULL, '/user', 'ep:user', 1, 90, 'setting_user', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705403612024643585, 0, 'admin', '2023-09-23 10:08:00', 'admin', '2024-08-11 12:35:34', 0, 1705397288498995201, '岗位管理', 'post', 0, NULL, NULL, '/post', 'icon-uv', 1, 80, 'setting_post', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705404432669581313, 0, 'admin', '2023-09-23 10:11:00', 'admin', '2024-08-10 18:39:18', 0, 1705397288498995201, '角色管理', 'role', 0, NULL, NULL, '/role', 'icon-role', 1, 60, 'setting_role', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705404534842826753, 0, 'admin', '2023-09-23 10:11:00', 'admin', '2024-08-12 14:41:23', 0, 1705397288498995201, '应用管理', 'app', 0, NULL, NULL, '/app', 'icon-goods-list', 1, 50, 'setting_app', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705405518658772993, 0, 'admin', '2023-09-23 10:15:00', 'admin', '2024-08-07 19:35:46', 0, 1705397288498995201, '部门管理', 'department', 0, NULL, NULL, '/department', 'ep:wind-power', 1, 40, 'setting_department', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705405650779348993, 0, 'admin', '2023-09-23 10:16:00', 'admin', '2024-08-12 15:11:16', 0, 1705397288498995201, '行政区域', 'region', 0, NULL, NULL, '/region', 'icon-textarea', 1, 10, 'setting_region', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705405860804927489, 0, 'admin', '2023-09-23 10:16:00', 'admin', '2024-08-12 14:11:20', 0, 1705397288498995201, '扩展配置', 'configure', 0, NULL, NULL, '/configure', 'icon-component', 1, 70, 'setting_configure', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705405971014459394, 0, 'admin', '2023-09-23 10:17:00', 'admin', '2024-08-10 18:39:03', 0, 1705397288498995201, '字典管理', 'dict', 0, NULL, NULL, '/dict', 'ep:list', 1, 30, 'setting_dict', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1705409192441257985, 0, 'admin', '2023-09-23 10:30:00', 'admin', '2023-09-23 10:30:00', 0, 1705397288498995201, '菜单管理', 'menu', 0, NULL, NULL, '/menu', 'ep:menu', 1, 20, 'setting_menu', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1710476160198299650, 0, 'admin', '2023-10-07 10:04:00', 'admin', '2024-08-12 19:05:41', 0, 0, '消息中心', 'message', 0, NULL, NULL, '/message', 'ep:message', 1, 20, 'layout.base', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1710488066858471425, 0, 'admin', '2023-10-07 10:51:00', 'admin', '2024-08-12 19:05:24', 0, 1710476160198299650, '消息管理', 'list', 0, NULL, NULL, '/list', 'ep:chat-dot-square', 1, 20, 'message_list', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1734132265541500930, 0, 'admin', '2023-12-11 16:45:00', 'admin', '2024-08-12 19:05:05', 0, 1710476160198299650, '我的消息', 'my', 0, NULL, NULL, '/my', 'ep:message-box', 1, 10, 'message_my', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1818217918824460289, 0, 'admin', '2024-07-30 17:31:47', 'admin', '2024-08-15 22:22:11', 0, 0, 'Iframe 测试菜单', 'iframe-test', 1, NULL, 'https://aizuda.com/home', '/Iframe-test', 'icon-bug', 1, 3, 'iframe-page', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1818497082743914497, 0, 'admin', '2024-07-31 12:01:05', 'admin', '2024-08-15 22:22:07', 0, 0, '外链测试菜单', 'link-test', 2, NULL, 'https://aizuda.com/home', '/link-test', 'icon-bug', 1, 5, 'iframe-page', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1818549132156563458, 0, 'admin', '2024-07-31 15:27:55', NULL, NULL, 0, 0, '首页', 'home', 0, NULL, NULL, '/home', 'mdi:monitor-dashboard', 1, 100, 'home', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1824089134229397505, 0, 'admin', '2024-08-15 22:21:54', NULL, NULL, 0, 0, '飞龙 ElementUI 版本', 'ele', 2, NULL, 'https://boot.aizuda.com/', '/ele', 'icon-warehouse', 1, 1, 'iframe-page', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1843833708455759874, 1, 'admin', '2024-10-09 09:59:48', 'admin', '2024-10-09 09:59:55', 0, 0, '代码生成', 'gen', 0, NULL, NULL, '/gen', 'hugeicons:source-code-square', 1, 9, 'layout.base', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1843834040632053762, 1, 'admin', '2024-10-09 10:01:07', NULL, NULL, 0, 1843833708455759874, '数据源管理', 'gen_database', 0, NULL, NULL, '/gen/database', 'tabler:database', 1, 100, 'gen_database', NULL, 0, NULL, 0, '{}');
INSERT INTO `sys_resource` VALUES (1843834117584949249, 1, 'admin', '2024-10-09 10:01:25', 'admin', '2024-10-09 10:01:35', 0, 1843833708455759874, '模板管理', 'gen_template', 0, NULL, NULL, '/gen/template', 'tabler:template', 1, 90, 'gen_template', NULL, 0, NULL, 0, '{}');

-- ----------------------------
-- Table structure for sys_resource_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource_api`;
CREATE TABLE `sys_resource_api`  (
    `id` bigint NOT NULL COMMENT '主键ID',
    `resource_id` bigint NOT NULL COMMENT '资源ID',
    `code` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限编码',
    `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统资源接口' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_resource_api
-- ----------------------------
INSERT INTO `sys_resource_api` VALUES (1883685191276810241, 1705141694319927298, 'sys:dict:listParent', '父级字典列表');
INSERT INTO `sys_resource_api` VALUES (1883685354997272578, 1818549132156563458, 'sys:sse:connect', 'SSE连接');
INSERT INTO `sys_resource_api` VALUES (1883687477814218753, 1710488066858471425, 'sys:message:', '系统消息相关接口');
INSERT INTO `sys_resource_api` VALUES (1883688099460403201, 1734132265541500930, 'sys:message:', '系统消息相关接口');
INSERT INTO `sys_resource_api` VALUES (1883688988191477762, 1705403264375562242, 'sys:department:listTree', '部门树列表');
INSERT INTO `sys_resource_api` VALUES (1883689161240072193, 1705403264375562242, 'sys:user:', '系统用户相关接口');
INSERT INTO `sys_resource_api` VALUES (1883690404024930306, 1705403612024643585, 'sys:post:', '岗位相关接口');
INSERT INTO `sys_resource_api` VALUES (1883690965633847298, 1705405860804927489, 'sys:configure:', '扩展配置相关接口');
INSERT INTO `sys_resource_api` VALUES (1883691408577515521, 1705404432669581313, 'sys:role:', '系统角色相关接口');
INSERT INTO `sys_resource_api` VALUES (1883692520927907841, 1705404534842826753, 'sys:app:', '应用分页相关接口');
INSERT INTO `sys_resource_api` VALUES (1883693031550865410, 1705405518658772993, 'sys:department:', '部门相关接口');
INSERT INTO `sys_resource_api` VALUES (1883693238829174786, 1705405518658772993, 'sys:user:page', '系统用户分页列表');
INSERT INTO `sys_resource_api` VALUES (1884133963064614914, 1705405971014459394, 'sys:dict:', '系统字典相关接口');
INSERT INTO `sys_resource_api` VALUES (1884134174050689026, 1818549132156563458, 'sys:dict:listSelectOptions', '通过字典编码查询表单下拉选择项列表');
INSERT INTO `sys_resource_api` VALUES (1884134613500502017, 1705409192441257985, 'sys:resource:', '系统资源相关接口');
INSERT INTO `sys_resource_api` VALUES (1884135290880598018, 1705405650779348993, 'sys:region:', '行政区域相关接口');
INSERT INTO `sys_resource_api` VALUES (1884136159525150722, 1843834040632053762, 'gen:database:', '代码生成数据源相关接口');
INSERT INTO `sys_resource_api` VALUES (1884136289955422210, 1843834117584949249, 'gen:template:', '代码生成模板相关接口');
INSERT INTO `sys_resource_api` VALUES (1884136508323471361, 1843834040632053762, 'gen:table:', '预览下载相关接口');
INSERT INTO `sys_resource_api` VALUES (1884575070248448001, 1705141694319927298, 'sys:user:', '用户相关接口');
INSERT INTO `sys_resource_api` VALUES (1884575133922177025, 1705141694319927298, 'sys:role:', '角色相关接口');
INSERT INTO `sys_resource_api` VALUES (1884575326675611649, 1705141694319927298, 'sys:department:', '部门相关接口');
INSERT INTO `sys_resource_api` VALUES (1884937175959023618, 1705141694319927298, 'sys:region:', '行政区域相关接口');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `alias` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '别名',
    `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `status` smallint NOT NULL DEFAULT 1 COMMENT '状态 0、禁用 1、正常',
    `sort` int NOT NULL DEFAULT 0 COMMENT '排序'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1778231636381663234, 0, 'admin', '2024-04-11 09:20:35', 'aizuda', '2024-11-09 15:27:40', 0, 'CEO', 'ceo', NULL, 1, 7);
INSERT INTO `sys_role` VALUES (0, 0, 'admin', '2021-11-04 12:16:50', 'aizuda', '2024-11-09 15:27:45', 0, '系统管理员', 'systemAdmin', '管理角色', 1, 6);
INSERT INTO `sys_role` VALUES (1456247408778260482, 0, 'admin', '2021-11-04 21:10:00', 'admin', '2024-11-26 21:18:55', 0, '技术总监', 'technicalDirector', '技术总监', 1, 5);
INSERT INTO `sys_role` VALUES (1492884198322536450, 0, 'admin', '2022-02-13 23:31:00', 'admin', '2024-11-26 21:19:06', 0, '财务总监', 'financialOfficer', '财务总监', 1, 3);
INSERT INTO `sys_role` VALUES (1778264458643374082, 0, 'admin', '2024-04-11 11:31:00', 'admin', '2024-11-26 21:19:10', 0, '开发人员', 'developer', NULL, 1, 1);
INSERT INTO `sys_role` VALUES (1778246292458438657, 0, 'admin', '2024-04-11 10:18:49', 'admin', '2024-11-26 21:19:15', 0, '考勤管理员', '考勤管理员', NULL, 1, 1);
INSERT INTO `sys_role` VALUES (1, 0, '0', '2021-11-04 20:15:35', 'admin', '2024-11-26 21:19:23', 0, '普通员工', 'employees', '普通员工', 1, 1);
INSERT INTO `sys_role` VALUES (1696505296993959937, 0, 'admin', '2023-08-29 20:49:17', 'admin', '2024-11-26 21:19:35', 0, '测试人员', 'testingPersonnel', '测试人员', 0, 1);
INSERT INTO `sys_role` VALUES (1778266027724111874, 0, 'admin', '2024-04-11 11:37:14', 'admin', '2024-11-26 21:20:02', 0, '部门领导', '部门领导', NULL, 1, 1);

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource`  (
    `id` bigint NOT NULL COMMENT '主键ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `resource_id` bigint NOT NULL COMMENT '资源ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色资源' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO `sys_role_resource` VALUES (1855150242943774721, 1778231636381663234, 1818549132156563458);
INSERT INTO `sys_role_resource` VALUES (1855150242947969032, 1778231636381663234, 1710476160198299650);
INSERT INTO `sys_role_resource` VALUES (1855150242947969033, 1778231636381663234, 1710488066858471425);
INSERT INTO `sys_role_resource` VALUES (1855150242952163329, 1778231636381663234, 1734132265541500930);
INSERT INTO `sys_role_resource` VALUES (1855150242952163330, 1778231636381663234, 1705397288498995201);
INSERT INTO `sys_role_resource` VALUES (1855150242952163331, 1778231636381663234, 1705403264375562242);
INSERT INTO `sys_role_resource` VALUES (1855150242952163332, 1778231636381663234, 1705403612024643585);
INSERT INTO `sys_role_resource` VALUES (1855150242952163333, 1778231636381663234, 1705405860804927489);
INSERT INTO `sys_role_resource` VALUES (1855150242952163334, 1778231636381663234, 1705404432669581313);
INSERT INTO `sys_role_resource` VALUES (1855150242952163335, 1778231636381663234, 1705404534842826753);
INSERT INTO `sys_role_resource` VALUES (1855150242952163336, 1778231636381663234, 1705405518658772993);
INSERT INTO `sys_role_resource` VALUES (1855150242956357633, 1778231636381663234, 1705405971014459394);
INSERT INTO `sys_role_resource` VALUES (1855150242956357634, 1778231636381663234, 1705409192441257985);
INSERT INTO `sys_role_resource` VALUES (1855150242956357635, 1778231636381663234, 1705405650779348993);
INSERT INTO `sys_role_resource` VALUES (1855150242956357636, 1778231636381663234, 1843833708455759874);
INSERT INTO `sys_role_resource` VALUES (1855150242956357637, 1778231636381663234, 1843834040632053762);
INSERT INTO `sys_role_resource` VALUES (1855150242956357638, 1778231636381663234, 1843834117584949249);
INSERT INTO `sys_role_resource` VALUES (1855150242956357639, 1778231636381663234, 1818497082743914497);
INSERT INTO `sys_role_resource` VALUES (1855150242956357640, 1778231636381663234, 1818217918824460289);
INSERT INTO `sys_role_resource` VALUES (1855150242956357641, 1778231636381663234, 1824089134229397505);
INSERT INTO `sys_role_resource` VALUES (1855150264540246017, 0, 1818549132156563458);
INSERT INTO `sys_role_resource` VALUES (1855150264540246029, 0, 1710476160198299650);
INSERT INTO `sys_role_resource` VALUES (1855150264540246030, 0, 1710488066858471425);
INSERT INTO `sys_role_resource` VALUES (1855150264544440322, 0, 1734132265541500930);
INSERT INTO `sys_role_resource` VALUES (1855150264544440323, 0, 1705397288498995201);
INSERT INTO `sys_role_resource` VALUES (1855150264544440324, 0, 1705403264375562242);
INSERT INTO `sys_role_resource` VALUES (1855150264544440325, 0, 1705403612024643585);
INSERT INTO `sys_role_resource` VALUES (1855150264544440326, 0, 1705405860804927489);
INSERT INTO `sys_role_resource` VALUES (1855150264544440327, 0, 1705404432669581313);
INSERT INTO `sys_role_resource` VALUES (1855150264544440328, 0, 1705404534842826753);
INSERT INTO `sys_role_resource` VALUES (1855150264544440329, 0, 1705405518658772993);
INSERT INTO `sys_role_resource` VALUES (1855150264544440330, 0, 1705405971014459394);
INSERT INTO `sys_role_resource` VALUES (1855150264544440331, 0, 1705409192441257985);
INSERT INTO `sys_role_resource` VALUES (1855150264544440332, 0, 1705405650779348993);
INSERT INTO `sys_role_resource` VALUES (1855150264544440333, 0, 1843833708455759874);
INSERT INTO `sys_role_resource` VALUES (1855150264544440334, 0, 1843834040632053762);
INSERT INTO `sys_role_resource` VALUES (1855150264544440335, 0, 1843834117584949249);
INSERT INTO `sys_role_resource` VALUES (1855150264544440336, 0, 1818497082743914497);
INSERT INTO `sys_role_resource` VALUES (1855150264544440337, 0, 1818217918824460289);
INSERT INTO `sys_role_resource` VALUES (1855150264544440338, 0, 1824089134229397505);
INSERT INTO `sys_role_resource` VALUES (1861399234325618694, 1456247408778260482, 1710488066858471425);
INSERT INTO `sys_role_resource` VALUES (1861399234325618695, 1456247408778260482, 1734132265541500930);
INSERT INTO `sys_role_resource` VALUES (1861399234325618698, 1456247408778260482, 1710476160198299650);
INSERT INTO `sys_role_resource` VALUES (1861399234325618699, 1456247408778260482, 1818549132156563458);
INSERT INTO `sys_role_resource` VALUES (1861399278491639817, 1492884198322536450, 1818549132156563458);
INSERT INTO `sys_role_resource` VALUES (1861399295696674824, 1778264458643374082, 1818549132156563458);
INSERT INTO `sys_role_resource` VALUES (1861399318207504393, 1778246292458438657, 1818549132156563458);
INSERT INTO `sys_role_resource` VALUES (1861399349182439433, 1, 1818549132156563458);
INSERT INTO `sys_role_resource` VALUES (1861399399719608328, 1696505296993959937, 1818549132156563458);
INSERT INTO `sys_role_resource` VALUES (1861399512894513162, 1778266027724111874, 1710488066858471425);
INSERT INTO `sys_role_resource` VALUES (1861399512894513163, 1778266027724111874, 1734132265541500930);
INSERT INTO `sys_role_resource` VALUES (1861399512894513166, 1778266027724111874, 1710476160198299650);
INSERT INTO `sys_role_resource` VALUES (1861399512894513167, 1778266027724111874, 1818549132156563458);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
    `id` bigint NOT NULL COMMENT '主键 ID',
    `create_id` bigint NOT NULL COMMENT '创建人ID',
    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted` smallint NOT NULL DEFAULT 0 COMMENT '删除 0、否 1、是',
    `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
    `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `salt` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '随机盐',
    `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实名称',
    `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
    `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
    `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '性别',
    `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
    `phone_verified` smallint NOT NULL DEFAULT 0 COMMENT '手机号是否验证 0、否 1、是',
    `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
    `email_verified` smallint NOT NULL DEFAULT 0 COMMENT '邮箱是否验证 0、否 1、是',
    `status` smallint NOT NULL DEFAULT 1 COMMENT '状态 0、禁用 1、正常',
    `job_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (0, 0, '青苗', '2021-11-02 00:32:16', 'admin', '2024-05-02 13:25:14', 0, 'admin', '619798f74b24a7f75aad6032cb13ab32', '9262Q6l8', 'CEO', 'CEO', 'https://lolicode.gitee.io/scui-doc/demo/img/avatar.jpg', '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO `sys_user` VALUES (1, 0, 'admin', '2024-06-10 17:14:09', 'admin', '2024-06-10 17:14:36', 0, 'aizuda', '22178093497d6dd251bf667f757a1a28', 'V6438t97', 'AIZUDA', NULL, NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO `sys_user` VALUES (1778235419958444034, 0, 'admin', '2024-04-11 09:35:37', 'admin', '2024-05-02 13:25:14', 0, 'test03', '46c4c7007ee6b3403d5726f371e694b4', '34981X24', '笪小琴', '人事主管', NULL, '女', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO `sys_user` VALUES (1778235567803465730, 0, 'admin', '2024-04-11 09:36:12', 'admin', '2024-05-02 13:25:14', 0, 'test04', '8022258b8833dd871b73df3a84f32047', 't8lpphzD', '陈小超', '财务', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO `sys_user` VALUES (1778236021094481921, 0, 'admin', '2024-04-11 09:38:00', 'admin', '2024-05-02 13:25:14', 0, 'test01', 'be03be15c4dc9fd4e03b6af45710a251', '1O283K29', '杨小凤', '部门领导1', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO `sys_user` VALUES (1778236187025342466, 0, 'admin', '2024-04-11 09:38:40', 'admin', '2024-05-02 13:25:14', 0, 'test02', '9682d343fb1edddc09ecff80db8ee4f8', '9D9mzUHs', '夏小华', '部门领导2', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO `sys_user` VALUES (1778248547756670978, 0, 'admin', '2024-04-11 10:27:47', 'admin', '2024-05-02 13:25:14', 0, 'test05', '522663acf3ebf865be8ee302562f6200', '1L3n01Uh', '李小广', '开发人员1', NULL, '男', NULL, 0, NULL, 0, 1, NULL);
INSERT INTO `sys_user` VALUES (1778264912529981441, 0, 'admin', '2024-04-11 11:32:48', 'admin', '2024-05-02 13:25:14', 0, 'test06', 'b0b63f9e96e37de8b31fb07168d84205', '44r6N95v', '陈小辉', '开发人员2', NULL, '男', NULL, 0, NULL, 0, 1, NULL);

-- ----------------------------
-- Table structure for sys_user_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_department`;
CREATE TABLE `sys_user_department`  (
    `id` bigint NOT NULL COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `department_id` bigint NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_department
-- ----------------------------
INSERT INTO `sys_user_department` VALUES (1497974292503031810, 1, 1456247408778260482);
INSERT INTO `sys_user_department` VALUES (1694693758976966658, 1694693758968578049, 1456247408778260482);
INSERT INTO `sys_user_department` VALUES (1694693758976966659, 1694693758968578049, 1492884198322536450);
INSERT INTO `sys_user_department` VALUES (1694693758981160961, 1694693758968578049, 1);
INSERT INTO `sys_user_department` VALUES (1694693758981160962, 1694693758968578049, 0);
INSERT INTO `sys_user_department` VALUES (1694694001726504962, 1694694001718116354, 0);
INSERT INTO `sys_user_department` VALUES (1694753487552036866, 1694524980297252866, 1456247408778260482);
INSERT INTO `sys_user_department` VALUES (1694754654189305858, 1694523894970761218, 1);
INSERT INTO `sys_user_department` VALUES (1703707996831719425, 1694698980486987777, 0);
INSERT INTO `sys_user_department` VALUES (1703707996831719426, 1694698980486987777, 1456247408778260482);
INSERT INTO `sys_user_department` VALUES (1703707996831719427, 1694698980486987777, 1492884198322536450);
INSERT INTO `sys_user_department` VALUES (1703707996831719428, 1694698980486987777, 1);
INSERT INTO `sys_user_department` VALUES (1703707996835913730, 1694698980486987777, 1696505296993959937);
INSERT INTO `sys_user_department` VALUES (1705068256192471041, 1705067852272607233, 0);
INSERT INTO `sys_user_department` VALUES (1705068256196665345, 1705067852272607233, 1696505296993959937);
INSERT INTO `sys_user_department` VALUES (1705068256196665346, 1705067852272607233, 1);
INSERT INTO `sys_user_department` VALUES (1705068256196665347, 1705067852272607233, 1492884198322536450);
INSERT INTO `sys_user_department` VALUES (1705068256196665348, 1705067852272607233, 1456247408778260482);
INSERT INTO `sys_user_department` VALUES (1771165796622028802, 1694523277028147201, 1740953843466010625);
INSERT INTO `sys_user_department` VALUES (1776867375126446082, 1773278598010699778, 3);
INSERT INTO `sys_user_department` VALUES (1778234707618824193, 1749994976754032641, 1696349127893630978);
INSERT INTO `sys_user_department` VALUES (1778234753261240321, 1749994885959933953, 3);
INSERT INTO `sys_user_department` VALUES (1778238506618060801, 0, 3);
INSERT INTO `sys_user_department` VALUES (1778258222266974210, 1778236187025342466, 2);
INSERT INTO `sys_user_department` VALUES (1778258250159095810, 1778236021094481921, 2);
INSERT INTO `sys_user_department` VALUES (1778258364286107649, 1778235419958444034, 1778247724637093889);
INSERT INTO `sys_user_department` VALUES (1778258417193058305, 1778235567803465730, 1778237792399392769);
INSERT INTO `sys_user_department` VALUES (1778264717008306177, 1778248547756670978, 2);
INSERT INTO `sys_user_department` VALUES (1778264912542564353, 1778264912529981441, 1778247413147107329);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
    `id` bigint NOT NULL COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1497974292503031810, 1, 0);
INSERT INTO `sys_user_role` VALUES (1694693758976966658, 1694693758968578049, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1694693758976966659, 1694693758968578049, 1492884198322536450);
INSERT INTO `sys_user_role` VALUES (1694693758981160961, 1694693758968578049, 1);
INSERT INTO `sys_user_role` VALUES (1694693758981160962, 1694693758968578049, 0);
INSERT INTO `sys_user_role` VALUES (1694694001726504962, 1694694001718116354, 0);
INSERT INTO `sys_user_role` VALUES (1694753487552036866, 1694524980297252866, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1694754654189305858, 1694523894970761218, 1);
INSERT INTO `sys_user_role` VALUES (1703707996831719425, 1694698980486987777, 0);
INSERT INTO `sys_user_role` VALUES (1703707996831719426, 1694698980486987777, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1703707996831719427, 1694698980486987777, 1492884198322536450);
INSERT INTO `sys_user_role` VALUES (1703707996831719428, 1694698980486987777, 1);
INSERT INTO `sys_user_role` VALUES (1703707996835913730, 1694698980486987777, 1696505296993959937);
INSERT INTO `sys_user_role` VALUES (1705068256192471041, 1705067852272607233, 0);
INSERT INTO `sys_user_role` VALUES (1705068256196665345, 1705067852272607233, 1696505296993959937);
INSERT INTO `sys_user_role` VALUES (1705068256196665346, 1705067852272607233, 1);
INSERT INTO `sys_user_role` VALUES (1705068256196665347, 1705067852272607233, 1492884198322536450);
INSERT INTO `sys_user_role` VALUES (1705068256196665348, 1705067852272607233, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1711627814540976129, 1694320017608589314, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1711627814540976130, 1694320017608589314, 1492884198322536450);
INSERT INTO `sys_user_role` VALUES (1711627814540976131, 1694320017608589314, 1);
INSERT INTO `sys_user_role` VALUES (1764821434647240705, 1711682128658026497, 1);
INSERT INTO `sys_user_role` VALUES (1764821434659823617, 1711682128658026497, 1696505296993959937);
INSERT INTO `sys_user_role` VALUES (1764821434659823618, 1711682128658026497, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1771165796622028801, 1694523277028147201, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1776867375105474561, 1773278598010699778, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1778234707597852673, 1749994976754032641, 1696505296993959937);
INSERT INTO `sys_user_role` VALUES (1778234753248657410, 1749994885959933953, 0);
INSERT INTO `sys_user_role` VALUES (1778258364273524737, 1778235419958444034, 1778246292458438657);
INSERT INTO `sys_user_role` VALUES (1778258417180475394, 1778235567803465730, 1492884198322536450);
INSERT INTO `sys_user_role` VALUES (1778264716999917569, 1778248547756670978, 1778264458643374082);
INSERT INTO `sys_user_role` VALUES (1778264912538370050, 1778264912529981441, 1778264458643374082);
INSERT INTO `sys_user_role` VALUES (1778266180442914818, 1778236021094481921, 1778266027724111874);
INSERT INTO `sys_user_role` VALUES (1800094198569537538, 1800094084849373185, 0);
INSERT INTO `sys_user_role` VALUES (1815367038679453697, 0, 1778231636381663234);
INSERT INTO `sys_user_role` VALUES (1815367038679453698, 0, 1456247408778260482);
INSERT INTO `sys_user_role` VALUES (1815367038679453699, 0, 1778266027724111874);
INSERT INTO `sys_user_role` VALUES (1815367038679453700, 0, 1778264458643374082);
INSERT INTO `sys_user_role` VALUES (1815367038679453701, 0, 0);
INSERT INTO `sys_user_role` VALUES (1815367090382639105, 1778236187025342466, 1778266027724111874);
INSERT INTO `sys_user_role` VALUES (1815367090382639106, 1778236187025342466, 1492884198322536450);

SET FOREIGN_KEY_CHECKS = 1;
