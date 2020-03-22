package com.xxbb.sorm.core;

import com.xxbb.sorm.bean.Configuration;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，维持连接对象的管理
 * @author xxbb
 */
public class DBManager {
    private  static ConnectionPool connectionPool=ConnectionPool.getInstance();
    private  static Configuration configuration=ConnectionPool.getInstance().getConfiguration();
    static{
        //初始化表类映射关系
        try {
            Class.forName("com.xxbb.sorm.core.TableContext");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static int getIdleCount(){
        return connectionPool.getIdleCount();
    }

    /**
     * 获取已创建连接数
     * @return 已创建连接数
     */
    public static int getCreatedCount(){
        return connectionPool.getCreatedCount();
    }

    /**
     * 获取配置信息
     * @return 配置信息对象
     */
    public static Configuration getConfiguration(){
        return configuration;
    }
    //

    /**
     * 获取连接
     * @return 数据库连接
     */
    public static Connection getConnection(){
        return connectionPool.getConnection();

    }

    /**
     * 以下为关闭数据库相关连接
     * @param conn 数据库连接对象
     */
    public static void closeConnection(Connection conn){
        if(conn!=null){
            connectionPool.returnConnection(conn);
        }
    }
    public static void closeAll(Connection conn, Statement stat, ResultSet rs){
        closeResultSet(rs);
        closeStatement(stat);
        closeConnection(conn);
    }
    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        closeResultSet(rs);
        closePreparedStatement(ps);
        closeConnection(conn);
    }

    public static void closeStatement(Statement stat){
        if(stat!=null){
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closePreparedStatement(PreparedStatement ps){
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closeResultSet(ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

