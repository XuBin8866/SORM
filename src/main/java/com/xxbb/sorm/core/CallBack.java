package com.xxbb.sorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 回调接口
 * @author xxbb
 */
public interface CallBack {
    /**
     * 具体的数据库操作
     * @param conn 连接对象
     * @param ps 预处理对象
     * @param rs 结果集对象
     * @return Object对象，可以接收多种数据类型，如List,以适应不同的情况
     */
    Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs);
}
