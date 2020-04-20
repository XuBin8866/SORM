package com.xxbb.test;

import com.xxbb.sorm.core.DBManager;
import org.junit.Test;


import java.sql.*;

public class ConnectionTest {
    @Test
    public void testQuery(){
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_orm?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8&allowPublicKeyRetrieval=true",
                    "root",
                    "123456");
            ps=connection.prepareStatement("SELECT t.*,u.* FROM t_teacher t left join t_user u on t.id=u.id");
            rs=ps.executeQuery();
            while(rs.next()){
                ResultSetMetaData resultSetMetaData=rs.getMetaData();
                for(int i=0;i<resultSetMetaData.getColumnCount();i++){
                    //获取列名和该列的值，从1开始，这里获取的是该列本身的名字
                    String columnName=resultSetMetaData.getColumnName(i+1);
                    Object columnValue= rs.getObject(i+1);
                    System.out.print(columnName+"="+columnValue+" ");
                }
                System.out.println();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            System.out.println("进行等待");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Connection c=DBManager.getConnection();
            System.out.println("获取到了连接"+Thread.currentThread().getName()+c);
        },"consumer").start();
        long start=System.currentTimeMillis();
        Connection conn=null;
        for(int i=0;i<20;i++){
            conn=DBManager.getConnection();
        }
        Thread.sleep(1000);
        DBManager.closeConnection(conn);
        long end=System.currentTimeMillis();
        System.out.println("执行时间："+(end-start));


    }
}
