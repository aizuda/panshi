/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 菜单
 */
@Getter
@Setter
public class MenuVO implements Serializable {
    private String name;
    private String redirect;
    private String path;
    private String component;
    private Map<String, Object> meta;
    List<MenuVO> children;

}
