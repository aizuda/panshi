/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.log;

import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.core.bean.BeanConvert;
import com.aizuda.service.web.UserSession;
import com.baomidou.kisso.common.IpHelper;
import lombok.Getter;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * <a href="http://aizuda.com">爱组搭</a>低代码组件化开发平台
 * ----------------------------------------
 * 操作日志
 *
 * @author 青苗
 * @since 1.1.0
 */
@Getter
public class Oplog implements BeanConvert {
    /**
     * 创建人ID
     */
    private Long createId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 服务
     */
    private String service;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作业务
     */
    private String business;

    /**
     * 操作方法
     */
    private String method;

    /**
     * 类型 0，成功 1，失败 2，异常
     */
    private Integer type;

    /**
     * 内容详情
     */
    private String content;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * URI地址
     */
    private String uri;

    /**
     * 耗时
     */
    private Long duration;

    /**
     * 设置服务名称
     *
     * @param service 服务
     */
    public Oplog service(String service) {
        this.service = service;
        return this;
    }

    /**
     * 设置操作模块
     *
     * @param module 操作模块
     */
    public Oplog module(String module) {
        this.module = module;
        return this;
    }

    /**
     * 设置操作业务
     *
     * @param business 操作业务
     */
    public Oplog business(String business) {
        this.business = business;
        return this;
    }

    /**
     * 设置内容详情
     *
     * @param content 内容详情
     */
    public Oplog content(String content) {
        this.content = content;
        return this;
    }

    /**
     * 设置审计信息
     *
     * @param userSession 登录用户信息
     */
    public Oplog audit(UserSession userSession) {
        if (null != userSession) {
            this.createId = userSession.getId();
            this.createBy = userSession.getUsername();
        }
        return this;
    }

    /**
     * 设置业务状态 true 成功 false 失败
     *
     * @param success 业务状态
     */
    public Oplog status(boolean success) {
        this.type = (success ? 0 : 1);
        return this;
    }

    /**
     * 设置逻辑失败
     */
    public Oplog failed() {
        return this.status(false);
    }

    /**
     * 设置异常信息
     *
     * @param exception 异常详情
     */
    public Oplog exception(String exception) {
        this.content = exception;
        this.type = 2;
        return this;
    }

    /**
     * 设置切面环绕通知
     *
     * @param signature {@link Signature}
     * @param request   {@link HttpServletRequest}
     * @param args      请求参数
     */
    public Oplog around(Signature signature, HttpServletRequest request, Object[] args) {
        if (null == this.createId) {
            // 尝试设置审计信息
            this.audit(UserSession.getLoginInfo(request, true));
        }
        if (null != args && args.length > 0) {
            this.params = JacksonUtils.toJSONString(args);
        }
        MethodSignature ms = (MethodSignature) signature;
        // 被切的方法
        Method method = ms.getMethod();
        // 返回类型
        Class<?> methodReturnType = method.getReturnType();
        if (!methodReturnType.isAssignableFrom(Void.TYPE)) {
            if (null != args && args.length > 0) {
                this.params = JacksonUtils.toJSONString(args);
            }
        }
        this.method = ms.getDeclaringTypeName() + "." + ms.getName();
        this.browser = request.getHeader("User-Agent");
        this.uri = request.getRequestURI();
        this.ip = IpHelper.getIpAddr(request);
        return this;
    }

    /**
     * 设置操作时间，并保存至本地线程
     */
    public Oplog build(long duration) {
        // 创建日期
        this.createTime = new Date();
        // 记录耗时
        this.duration = duration;
        // 保存至本地线程
        OplogContext.OPLOG_HOLDER.set(this);
        return this;
    }
}
