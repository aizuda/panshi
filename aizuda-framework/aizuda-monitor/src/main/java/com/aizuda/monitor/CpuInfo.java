/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.monitor;

import lombok.Getter;
import lombok.Setter;

/**
 * CPU 信息
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
@Getter
@Setter
public class CpuInfo {

    /**
     * 物理处理器数量
     */
    private int physicalProcessorCount;

    /**
     * 逻辑处理器数量
     */
    private int logicalProcessorCount;

    /**
     * 系统使用率
     */
    private double systemPercent;

    /**
     * 用户使用率
     */
    private double userPercent;

    /**
     * 当前等待率
     */
    private double waitPercent;

    /**
     * 当前使用率
     */
    private double usePercent;


}
