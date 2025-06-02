/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 自定义批量插入方法
 *
 * @author 青苗
 * @since 1.1.0
 */
public class InsertBatch extends AbstractMethod {

    public InsertBatch(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // INSERT INTO table (A, B, C) VALUES
        // <foreach collection="list" item="item" separator=",">
        // (#{item.a}, #{item.b}, #{item.c})
        // </foreach>
        KeyGenerator keyGenerator = new NoKeyGenerator();
        StringBuilder fieldBuilder = new StringBuilder("(");
        StringBuilder placeholderBuilder = new StringBuilder("(");
        String keyProperty = null;
        String keyColumn = null;

        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /** 自增主键 */
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                /** 用户输入自定义ID */
                fieldBuilder.append(tableInfo.getKeyColumn()).append(",");
                // 正常自定义主键策略
                placeholderBuilder.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
            }
        }

        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        int size = fieldList.size();
        for (TableFieldInfo fieldInfo : fieldList) {
            fieldBuilder.append(fieldInfo.getColumn());
            placeholderBuilder.append("#{item.").append(fieldInfo.getEl()).append("}");
            if (--size > 0) {
                fieldBuilder.append(",");
                placeholderBuilder.append(",");
            }
        }

        fieldBuilder.append(")");
        placeholderBuilder.append(")");
        String sql = String.format("<script>INSERT INTO %s %s VALUES <foreach collection=\"list\" item=\"item\" separator=\",\">%s</foreach></script>",
                tableInfo.getTableName(), fieldBuilder, placeholderBuilder);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, "insertBatch", sqlSource, keyGenerator, keyProperty, keyColumn);
    }
}
