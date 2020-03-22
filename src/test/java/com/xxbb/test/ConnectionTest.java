package com.xxbb.test;

import com.xxbb.sorm.core.DBManager;


import java.sql.Connection;

public class ConnectionTest {
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
