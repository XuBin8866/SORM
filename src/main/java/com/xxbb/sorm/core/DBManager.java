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
    private  static Configuration configuration;
    private  static ConnectionPool connectionPool=ConnectionPool.getInstance();
    static{
        Properties pros=new Properties();
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration=new Configuration();
        configuration.setUsingDB(pros.getProperty("usingDB"));
        configuration.setDriverClass(pros.getProperty("jdbc.driverClass"));
        configuration.setUrl(pros.getProperty("jdbc.url"));
        configuration.setUsername(pros.getProperty("jdbc.username"));
        configuration.setPassword(pros.getProperty("jdbc.password"));
        configuration.setSrcPath(pros.getProperty("srcPath"));
        configuration.setPoPackage(pros.getProperty("poPackage"));
        configuration.setCatalog(pros.getProperty("catalog"));
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
            com.xxbb.connectionpool1.ConnectionPool.getInstance().returnConnection(conn);
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

