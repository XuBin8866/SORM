package com.xxbb.sorm.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装的JDBC查询常用的操作
 * @author xxbb
 */
public class JDBCUtils {
    /**
     * 给PreparedStatement语句设置参数
     * @param ps
     * @param params
     */
    public static void handleParams(PreparedStatement ps,Object[] params){
        if(params!=null){
            for(int i=0;i<params.length;i++){
                try{
                    ps.setObject(1+i,params[i]);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            System.out.println("准备执行的sql语句："+ps);
        }
    }
}
