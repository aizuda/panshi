package com.aizuda.boot.modules.system.entity.vo;

import com.aizuda.boot.modules.system.entity.SysRegion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SysRegionVO extends SysRegion {
    /**
     * 父级单位
     */
    private String parentName;
    /**
     * 子单位
     */
    private List<SysRegionVO> children;

}
