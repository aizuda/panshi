package com.aizuda.boot.modules.common;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * 数据库健康检查
 *
 * @author hubin
 * @since 1.0.0
 */
@Slf4j
public class DbCheckHealth {

    /**
     * 获取数据库连接
     *
     * @param className 驱动类
     * @param url       连接地址
     * @param user      用户名
     * @param password  密码
     */
    public static Connection getConnection(String className, String url, String user, String password)
            throws ClassNotFoundException, SQLException {
        Class.forName(className);
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 测试数据库连接
     *
     * @param className     驱动类
     * @param url           连接地址
     * @param user          用户名
     * @param password      密码
     * @param errorConsumer 失败回调函数
     */
    public static void test(String className, String url, String user, String password, Consumer<String> errorConsumer) {
        Connection connection = null;
        try {
            connection = getConnection(className, url, user, password);
        } catch (Exception e) {
            errorConsumer.accept(e.getMessage());
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException s) {
                    log.error("数据库连接关闭失败", s);
                }
            }
        }
    }
}
