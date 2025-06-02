package com.aizuda.boot.modules.gen.service.impl;

import com.aizuda.boot.modules.gen.entity.GenDatabase;
import com.aizuda.boot.modules.gen.entity.GenTemplate;
import com.aizuda.boot.modules.gen.entity.dto.GenDTO;
import com.aizuda.boot.modules.gen.entity.vo.GenVO;
import com.aizuda.boot.modules.gen.service.IGenDatabaseService;
import com.aizuda.boot.modules.gen.service.IGenTableService;
import com.aizuda.boot.modules.gen.service.IGenTemplateService;
import com.aizuda.common.toolkit.CollectionUtils;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.core.bean.BaseEntity;
import com.aizuda.core.exception.ApiException;
import com.aizuda.service.web.UserSession;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.type.JdbcType;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成业务表 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class GenTableServiceImpl implements IGenTableService {
    private IGenDatabaseService genDatabaseService;
    private IGenTemplateService genTemplateService;
    private DataSourceProperties dsp;

    @Override
    public List<GenVO> preview(GenDTO dto) {
        List<GenTemplate> genTemplates = genTemplateService.listCheckByIds(dto.getTemplateIds());

        // 初始化数据连接配置
        ConfigBuilder configBuilder = this.buildConfigBuilder(dto);

        // 查询指定表信息
        List<TableInfo> tableInfos = configBuilder.getTableInfoList();
        ApiAssert.fail(CollectionUtils.isEmpty(tableInfos), "未找到指定表信息");
        TableInfo tableInfo = tableInfos.get(0);

        // 初始化模板引擎
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine().init(configBuilder);
        return genTemplates.stream().map(t -> {
            GenVO vo = new GenVO();
            try {
                Map<String, Object> objectMap = this.getObjectMap(configBuilder, tableInfo);
                vo.setTplName(t.getTplName());
                vo.setTplContent(templateEngine.writer(objectMap, tableInfo.getEntityName(), t.getTplContent()));
                vo.setOutFile(t.getOutFile());
                vo.setRemark(t.getRemark());
            } catch (Exception e) {
                ApiAssert.fail("模板【" + t.getTplName() + "】内容异常");
            }
            return vo;
        }).toList();
    }

    @Override
    public void download(HttpServletResponse response, GenDTO dto) {
        List<GenTemplate> genTemplates = genTemplateService.listCheckByIds(dto.getTemplateIds());

        // 初始化数据连接配置
        ConfigBuilder configBuilder = this.buildConfigBuilder(dto);

        // 查询指定表信息
        List<TableInfo> tableInfos = configBuilder.getTableInfoList();

        // 初始化模板引擎
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine().init(configBuilder);

        // 设置响应内容类型
        String fileName = "genCode";
        if (tableInfos.size() < 2) {
            fileName = dto.getTableName().replaceAll(",", "_");
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".zip\"");
        response.setContentType("application/zip");

        // 创建ZIP输出流
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for (TableInfo ti : tableInfos) {
                // 渲染模板
                for (GenTemplate gt : genTemplates) {
                    // 创建输出流
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Map<String, Object> objectMap = this.getObjectMap(configBuilder, ti);
                    String context = templateEngine.writer(objectMap, ti.getEntityName(), gt.getTplContent());
                    byteArrayOutputStream.write(context.getBytes());

                    // 创建文件内容
                    String packageName = configBuilder.getPackageConfig().getParent();
                    ZipEntry zipEntry = new ZipEntry(packageName.replace('.', '/') + "/" +
                            String.format(gt.getOutFile(), ti.getEntityName()));
                    zos.putNextEntry(zipEntry);
                    zos.write(byteArrayOutputStream.toByteArray());
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            ApiAssert.fail("生成文件异常");
        }
    }

    private ConfigBuilder buildConfigBuilder(GenDTO dto) {
        DataSourceConfig.Builder dataSource;
        if (null != dto.getDatabaseId() && dto.getDatabaseId() > 0) {
            GenDatabase gb = genDatabaseService.checkById(dto.getDatabaseId());
            dataSource = new DataSourceConfig.Builder(gb.url(), gb.getUsername(), gb.getPassword());
        } else {
            dataSource = new DataSourceConfig.Builder(dsp.getUrl(), dsp.getUsername(), dsp.getPassword());
        }

        // 全局配置
        String author = dto.getAuthor();
        if (StringUtils.isBlank(author)) {
            author = UserSession.getLoginInfo().getUsername();
        }
        GlobalConfig.Builder globalConfig = new GlobalConfig.Builder().author(author)
                .dateType(DateType.ONLY_DATE)
                .enableSwagger();
        // 包配置
        PackageConfig.Builder packageInfo = new PackageConfig.Builder().parent("com.aizuda.boot.modules." + dto.getModule());
        // 策略配置
        StrategyConfig.Builder strategy = new StrategyConfig.Builder();
        strategy.addInclude(Arrays.stream(dto.getTableName().split(",")).toList())
                .controllerBuilder().enableRestStyle().enableHyphenStyle()
                .serviceBuilder().superServiceClass("com.aizuda.service.service.IBaseService")
                .mapperBuilder().superClass("com.aizuda.service.mapper.CrudMapper")
                .entityBuilder().enableLombok().disableSerialVersionUID().addTableFills(
                        new Column("create_id", FieldFill.INSERT),
                        new Column("create_by", FieldFill.INSERT),
                        new Column("create_time", FieldFill.INSERT),
                        new Column("update_by", FieldFill.UPDATE),
                        new Column("update_time", FieldFill.UPDATE)
                ).addSuperEntityColumns("id", "create_id", "create_by", "create_time",
                        "update_by", "update_time", "deleted")
                .logicDeleteColumnName("deleted")
                .superClass(BaseEntity.class);
        try {
            // 注入配置
            InjectionConfig.Builder injection = new InjectionConfig.Builder();
            // 模板渲染引擎 new CodeTemplateEngine()
            return new ConfigBuilder(packageInfo.build(), dataSource.build(), strategy.build(), null,
                    globalConfig.build(), injection.build());
        } catch (Throwable t) {
            throw new ApiException("数据库连接初始化失败");
        }
    }

    private Map<String, Object> getObjectMap(ConfigBuilder config, TableInfo tableInfo) {

        // 验证注解
        this.validationAnnotations(tableInfo);

        // 策略配置
        StrategyConfig strategyConfig = config.getStrategyConfig();
        Map<String, Object> controllerData = strategyConfig.controller().renderData(tableInfo);
        Map<String, Object> objectMap = new HashMap<>(controllerData);
        Map<String, Object> mapperData = strategyConfig.mapper().renderData(tableInfo);
        objectMap.putAll(mapperData);
        Map<String, Object> serviceData = strategyConfig.service().renderData(tableInfo);
        objectMap.putAll(serviceData);
        Map<String, Object> entityData = strategyConfig.entity().renderData(tableInfo);
        objectMap.putAll(entityData);
        objectMap.put("config", config);
        objectMap.put("package", config.getPackageConfig().getPackageInfo());
        GlobalConfig globalConfig = config.getGlobalConfig();
        objectMap.put("author", globalConfig.getAuthor());
        objectMap.put("kotlin", globalConfig.isKotlin());
        objectMap.put("swagger", globalConfig.isSwagger());
        objectMap.put("springdoc", globalConfig.isSpringdoc());
        objectMap.put("date", globalConfig.getCommentDate());
        String schemaName = "";
        if (strategyConfig.isEnableSchema()) {
            schemaName = config.getDataSourceConfig().getSchemaName();
            if (StringUtils.isNotBlank(schemaName)) {
                schemaName = schemaName + ".";
                tableInfo.setConvert(true);
            }
        }

        objectMap.put("schemaName", schemaName);
        objectMap.put("table", tableInfo);
        objectMap.put("entity", tableInfo.getEntityName());
        return objectMap;
    }

    /**
     * 追加校验注解
     */
    protected void validationAnnotations(TableInfo tableInfo) {
        List<TableField> fields = tableInfo.getFields();
        fields.forEach(tableField -> {
            TableField.MetaInfo metaInfo = tableField.getMetaInfo();
            final String lt = "\t";
            final String lnt = "\n\t";
            final JdbcType jdbcType = metaInfo.getJdbcType();
            StringBuilder annotations = new StringBuilder();
            boolean tabLine = false;
            if (!metaInfo.isNullable()) {
                annotations.append(lt);
                if (JdbcType.VARCHAR == jdbcType) {
                    annotations.append("@NotBlank(groups = Create.class)");
                    tableInfo.addImportPackages("jakarta.validation.constraints.NotBlank");
                } else {
                    annotations.append("@NotNull(groups = Create.class)");
                    tableInfo.addImportPackages("jakarta.validation.constraints.NotNull");
                }
                tabLine = true;
            }
            if (JdbcType.VARCHAR == jdbcType) {
                annotations.append(tabLine ? lnt : lt);
                annotations.append("@Size(max = ").append(metaInfo.getLength()).append(")");
                tableInfo.addImportPackages("jakarta.validation.constraints.Size");
            } else if (JdbcType.BIGINT == jdbcType
                    || JdbcType.INTEGER == jdbcType
                    || JdbcType.TINYINT == jdbcType
                    || JdbcType.SMALLINT == jdbcType
                    || JdbcType.BIT == jdbcType
                    || JdbcType.FLOAT == jdbcType
                    || JdbcType.DOUBLE == jdbcType
                    || JdbcType.DECIMAL == jdbcType) {
                annotations.append(tabLine ? lnt : lt);
                annotations.append("@PositiveOrZero");
                tableInfo.addImportPackages("jakarta.validation.constraints.PositiveOrZero");
            }
            Map<String, Object> customMap = tableField.getCustomMap();
            if (MapUtils.isEmpty(customMap)) {
                customMap = new HashMap<>();
            }
            String va = annotations.toString();
            if (StringUtils.isNotBlank(va)) {
                va += lnt;
                tableInfo.addImportPackages("com.aizuda.core.validation.Create");
            } else {
                va = lt;
            }
            customMap.put("validationAnnotations", va);
            tableField.setCustomMap(customMap);
        });
    }
}
