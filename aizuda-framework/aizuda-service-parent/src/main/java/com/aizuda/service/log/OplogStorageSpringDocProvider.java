/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.log;

import com.aizuda.common.toolkit.AnnotationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 日志存储 Spring Doc 文档注解获取提供者
 *
 * @author 青苗
 * @since 1.1.0
 */
public abstract class OplogStorageSpringDocProvider implements IOplogStorageProvider {

    /**
     * 获取 Spring Doc 文档注解信息，保存日志
     *
     * @param signature {@link Signature}
     * @param oplog     操作日志
     */
    @Override
    public void save(Signature signature, Oplog oplog) {
        // 操作模块
        if (null == oplog.getModule()) {
            Tag tag = AnnotationUtils.get(signature.getDeclaringType(), Tag.class);
            if (null != tag) {
                oplog.module(tag.name());
            }
        }
        // 操作业务
        if (null == oplog.getBusiness() && signature instanceof MethodSignature) {
            MethodSignature ms = (MethodSignature) signature;
            Operation operation = AnnotationUtils.get(ms.getMethod(), Operation.class);
            if (null != operation) {
                oplog.business(operation.summary());
            }
        }
        // 保存操作日志
        this.save(oplog);
    }

    /**
     * 保存日志
     *
     * @param oplog 操作日志
     */
    public abstract void save(Oplog oplog);
}
