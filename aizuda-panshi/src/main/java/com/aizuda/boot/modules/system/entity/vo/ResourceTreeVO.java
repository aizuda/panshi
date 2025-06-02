/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.entity.vo;

import com.aizuda.boot.modules.system.entity.SysResource;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResourceTreeVO extends SysResource {
    /**
     * 父级菜单名
     */
    private String parentName;
    /**
     * 子类资源
     */
    private List<ResourceTreeVO> children;

}
