/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.monitor;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作系统信息
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
@Getter
@Setter
public class SysInfo {

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统 ip
     */
    private String ip;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * 项目路径
     */
    private String userDir;

}
