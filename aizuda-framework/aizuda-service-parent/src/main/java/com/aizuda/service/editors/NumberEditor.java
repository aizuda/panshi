/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.editors;

import java.beans.PropertyEditorSupport;

public class NumberEditor extends PropertyEditorSupport {

    public NumberEditor() {
        // to do nothing
    }

    @Override
    public String getJavaInitializationString() {
        Object var1 = this.getValue();
        return var1 != null ? var1.toString() : "null";
    }
}
