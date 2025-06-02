/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.system.service.impl;

import com.aizuda.boot.modules.system.entity.SysUser;
import com.aizuda.boot.modules.system.entity.dto.AssignDepartmentsDTO;
import com.aizuda.boot.modules.system.entity.dto.AssignRolesDTO;
import com.aizuda.boot.modules.system.entity.dto.ResetPasswordDTO;
import com.aizuda.boot.modules.system.entity.dto.SysUserDTO;
import com.aizuda.boot.modules.system.entity.vo.SysUserRelIdsVO;
import com.aizuda.boot.modules.system.entity.vo.SysUserVO;
import com.aizuda.boot.modules.system.mapper.SysUserMapper;
import com.aizuda.boot.modules.system.service.ISysUserDepartmentService;
import com.aizuda.boot.modules.system.service.ISysUserRoleService;
import com.aizuda.boot.modules.system.service.ISysUserService;
import com.aizuda.common.toolkit.RegexUtils;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.aizuda.service.web.UserSession;
import com.baomidou.kisso.common.encrypt.MD5;
import com.baomidou.kisso.common.encrypt.MD5Salt;
import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 系统用户 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Resource
    private ISysUserRoleService sysUserRoleService;
    @Resource
    private ISysUserDepartmentService sysUserDepartmentService;

    @Override
    public Page<SysUser> page(Page<SysUser> page, SysUserVO vo) {
        Page<SysUser> sysUserPage = baseMapper.selectPageByVO(page, vo);
        if (sysUserPage.getTotal() > 0) {
            // 对外隐藏属性
            this.hiddenData(sysUserPage.getRecords());
        }
        return sysUserPage;
    }

    /**
     * 隐藏数据
     */
    protected List<SysUser> hiddenData(List<SysUser> sysUsers) {
        sysUsers.forEach(t -> {
            t.setPassword(null);
            t.setSalt(null);
        });
        return sysUsers;
    }

    @Override
    public List<SysUser> list20ByUsername(String username) {
        LambdaQueryWrapper<SysUser> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(username)) {
            lqw.like(SysUser::getUsername, username);
            lqw.or().like(SysUser::getRealName, username);
        }
        return this.hiddenData(super.page(Page.of(1, 20, false), lqw).getRecords());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(SysUserDTO dto) {
        ApiAssert.fail(!RegexUtils.matches("(^[A-Za-z]{6,50}$)|(^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,50}$)", dto.getUsername()),
                "用户名，必须是 6 到 50 位 字母 或者 字母数字混合 字符串");
        this.checkPassword(dto.getPassword());
        // 检查登录账号是否存在
        this.checkExists(Wrappers.<SysUser>lambdaQuery().select(SysUser::getId).eq(SysUser::getUsername, dto.getUsername()), "登录账号已存在");
        dto.setSalt(RandomUtil.getCharacterAndNumber(8));
        dto.setPassword(this.encodePassword(dto.getUsername(), dto.getSalt(), dto.getPassword()));
        ApiAssert.fail(!super.save(dto), "用户信息保存失败");
        if (CollectionUtils.isNotEmpty(dto.getRoleIds())) {
            ApiAssert.fail(!this.assignRoles(dto), "角色分配保存失败");
        }
        if (CollectionUtils.isNotEmpty(dto.getDepartmentIds())) {
            ApiAssert.fail(!this.assignDepartments(dto), "部门分配保存失败");
        }
        return true;
    }

    protected void checkPassword(String password) {
        ApiAssert.fail(!RegexUtils.isPassword(password), "登录密码必须为6-20位大小写字母数字特殊字符组合不包含@符合");
    }

    protected String encodePassword(String username, String salt, String password) {
        return MD5Salt.encode(username + salt, MD5.toMD5(password));
    }

    protected boolean assignRoles(SysUserDTO dto) {
        // 分配角色
        AssignRolesDTO ard = new AssignRolesDTO();
        ard.setRoleIds(dto.getRoleIds());
        ard.setUserIds(Collections.singletonList(dto.getId()));
        return sysUserRoleService.assignRoles(ard);
    }

    protected boolean assignDepartments(SysUserDTO sud) {
        // 分配部门
        AssignDepartmentsDTO dto = new AssignDepartmentsDTO();
        dto.setDepartmentIds(sud.getDepartmentIds());
        dto.setUserIds(Collections.singletonList(sud.getId()));
        return sysUserDepartmentService.assignDepartments(dto);
    }

    @Override
    public boolean updateById(SysUserDTO dto) {
        ApiAssert.fail(!super.updateById(dto), "用户信息保存失败");
        // 登录账号，密码，盐不允许更新
        dto.setUsername(null);
        dto.setPassword(null);
        dto.setSalt(null);
        ApiAssert.fail(!this.assignRoles(dto), "角色分配保存失败");
        ApiAssert.fail(!this.assignDepartments(dto), "部门分配保存失败");
        return true;
    }

    @Override
    public boolean updateStatusById(Long id, Integer status) {
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setStatus(Objects.equals(status, 1) ? 1 : 0);
        return super.updateById(sysUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean resetPassword(ResetPasswordDTO dto) {
        this.checkPassword(dto.getPassword());
        List<Long> ids = dto.getIds();
        for (Long id : ids) {
            SysUser sysUser = super.getOne(Wrappers.<SysUser>lambdaQuery()
                    .select(SysUser::getUsername, SysUser::getSalt)
                    .eq(SysUser::getId, id));
            if (null == sysUser) {
                continue;
            }
            SysUser temp = new SysUser();
            temp.setId(id);
            temp.setPassword(this.encodePassword(sysUser.getUsername(), sysUser.getSalt(), dto.getPassword()));
            super.updateById(temp);
        }
        return true;
    }

    @Override
    public SysUser getById(Long id) {
        if (null == id) {
            // 未指定ID读取当前登录用户信息
            id = UserSession.getLoginInfo().getId();
        }
        SysUser sysUser = baseMapper.selectById(id);
        if (null != sysUser) {
            // 隐藏敏感数据
            sysUser.setSalt(null);
            sysUser.setPassword(null);
        }
        return sysUser;
    }

    @Override
    public SysUserRelIdsVO getRelIdsById(Long id) {
        SysUserRelIdsVO vo = new SysUserRelIdsVO();
        vo.setRoleIds(sysUserRoleService.listRoleIdsByUserId(id));
        vo.setDepartmentIds(sysUserDepartmentService.listDepartmentIdsByUserId(id));
        return vo;
    }
}
