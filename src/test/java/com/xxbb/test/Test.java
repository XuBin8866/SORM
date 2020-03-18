package com.xxbb.test;

import com.xxbb.po.User;
import com.xxbb.sorm.core.ConnectionPool;
import com.xxbb.sorm.core.MySqlQuery;
import com.xxbb.sorm.core.QueryFactory;
import com.xxbb.vo.UserVo;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        QueryFactory qf=QueryFactory.getInstance();
        unionQueryTest((MySqlQuery) qf.createQuery());
        long end=System.currentTimeMillis();
        System.out.println("执行时间："+(end-start));
    }
    public static void poolEfficienyTest(int num){
        for(int i=0;i<num;i++){
            QueryFactory qf=QueryFactory.getInstance();
            MySqlQuery mq=(MySqlQuery) qf.createQuery();
            queryTest(mq);
        }
    }

    public static void queryValueTest(MySqlQuery mq){
        User u = new User();
        u.setId(3);
        u.setUsername("xxbb");
        u.setPassword("123456");
        Object res=mq.queryValue("select t.username from t_user t  where t.username=?",new String[]{u.getUsername()});
        System.out.println(res);
    }
    public static void unionQueryTest(MySqlQuery mq){
        UserVo u = new UserVo();
        u.setId(3);
        u.setUsername("xxbb");
        u.setPassword("123456");
        Object res=mq.queryUniqueRows("select t.id,t.username,t.password,t.if_freeze,p.role from t_user t left join t_purview p on p.id=t.id where t.id=? and t.username=? and t.password=?",u.getClass(),new String[]{u.getId().toString(),u.getUsername(),u.getPassword()});
        System.out.println(res);
    }
    public static void queryTest(MySqlQuery mq){
            User u = new User();
            u.setId(3);
            u.setUsername("xxbb");
            u.setPassword("123456");
            List res=mq.queryRows("select t.id,t.username,t.password from t_user t  where t.id=? and t.username=? and t.password=?",u.getClass(),new String[]{u.getId().toString(),u.getUsername(),u.getPassword()});
            System.out.println(res);
    }
    public static void queryUniqueTest(MySqlQuery mq){
        User u = new User();
        u.setId(3);
        u.setUsername("xxbb");
        u.setPassword("123456");
        Object res=mq.queryUniqueRows("select t.id,t.username,t.password from t_user t  where t.id=? and t.username=? and t.password=?",u.getClass(),new String[]{u.getId().toString(),u.getUsername(),u.getPassword()});
        System.out.println(res);
    }
}
