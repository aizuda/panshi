package com.aizuda.boot.modules.gen.service.impl;

import com.aizuda.boot.modules.common.DbCheckHealth;
import com.aizuda.boot.modules.common.vo.SelectOptionVO;
import com.aizuda.boot.modules.gen.entity.GenDatabase;
import com.aizuda.boot.modules.gen.mapper.GenDatabaseMapper;
import com.aizuda.boot.modules.gen.service.IGenDatabaseService;
import com.aizuda.core.api.ApiAssert;
import com.aizuda.service.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成数据源表 服务实现类
 *
 * @author 青苗
 * @since 1.0.0
 */
@Service
public class GenDatabaseServiceImpl extends BaseServiceImpl<GenDatabaseMapper, GenDatabase> implements IGenDatabaseService {

    @Override
    public Page<GenDatabase> page(Page<GenDatabase> page, GenDatabase genDatabase) {
        LambdaQueryWrapper<GenDatabase> lqw = Wrappers.lambdaQuery(genDatabase);
        Page<GenDatabase> genDatabasePage = super.page(page, lqw);
        if (CollectionUtils.isNotEmpty(genDatabasePage.getRecords())) {
            // 屏蔽数据源密码
            genDatabasePage.getRecords().forEach(t -> t.setPassword(null));
        }
        return genDatabasePage;
    }

    @Override
    public boolean save(GenDatabase genDatabase) {
        this.checkDatabase(genDatabase);
        return super.save(genDatabase);
    }

    private void checkDatabase(GenDatabase gd) {
        DbCheckHealth.test(gd.driverClassName(), gd.url(), gd.getUsername(), gd.getPassword(),
                error -> ApiAssert.fail("数据库配置有误，" + error));
    }

    @Override
    public boolean updateById(GenDatabase genDatabase) {
        ApiAssert.fail(null == genDatabase.getId(), "主键不存在无法更新");
        this.checkDatabase(genDatabase);
        return super.updateById(genDatabase);
    }

    @Override
    public GenDatabase getNoPasswordById(Long id) {
        GenDatabase genDatabase = super.getById(id);
        if (null != genDatabase) {
            // 屏蔽数据源密码
            genDatabase.setPassword(null);
        }
        return genDatabase;
    }

    @Override
    public List<SelectOptionVO> listSelectOptions() {
        List<SelectOptionVO> voList = new ArrayList<>();
        voList.add(SelectOptionVO.of(0L, "默认"));
        List<GenDatabase> genDatabases = lambdaQuery().select(GenDatabase::getId, GenDatabase::getAlias).list();
        if (CollectionUtils.isNotEmpty(genDatabases)) {
            voList.addAll(genDatabases.stream().map(t -> SelectOptionVO.of(t.getId(), t.getAlias())).toList());
        }
        return voList;
    }
}
