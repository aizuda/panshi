package com.aizuda.boot.modules.system.entity.vo;

import com.aizuda.boot.modules.system.entity.SysDepartment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysDepartmentVO extends SysDepartment {
    /**
     * 父级部门
     */
    private String parentName;
    /**
     * 子部门
     */
    private List<SysDepartmentVO> children;

}
