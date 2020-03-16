package com.xxbb.dao;

import com.xxbb.druidpool.JDBCTool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BaseDao {
    protected Connection conn;
    protected PreparedStatement ps;
    protected ResultSet rs;
    public BaseDao(){

    }
    public void close(){
        JDBCTool.closeAll(conn,ps,rs);
    }
    public ResultSet executeQuery(String sql,Object ... params){



        return rs;
    }

}
