/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.web;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AizudaMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        UserSession userSession = UserSession.getLoginInfo(true);
        if (null != userSession) {
            this.fillHasGetter(metaObject, "createId", userSession.getId());
            this.fillHasGetter(metaObject, "createBy", userSession.getUsername());
        }
        this.fillHasGetter(metaObject, "createTime", new Date());
        this.fillHasGetter(metaObject, "deleted", 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        UserSession userSession = UserSession.getLoginInfo(true);
        if (null != userSession) {
            this.fillHasGetter(metaObject, "updateBy", userSession.getUsername());
        }
        this.fillHasGetter(metaObject, "updateTime", new Date());
    }

    protected void fillHasGetter(MetaObject metaObject, String fieldName, Object fieldVal) {
        if (metaObject.hasGetter(fieldName)) {
            this.fillStrategy(metaObject, fieldName, fieldVal);
        }
    }
}
