package com.xxbb.sorm.core;

import com.xxbb.po.User;

import com.xxbb.vo.UserVo;


import java.util.List;

/**
 * 负责MySql数据库的操作
 * @author xxbb
 */
public class MySqlQuery extends BaseQuery {
    public static void main(String[] args) {
      queryValueTest();
    }
    public static void queryValueTest(){
        MySqlQuery mq = new MySqlQuery();
        User u = new User();
        u.setId(3);
        u.setUsername("xxbb");
        u.setPassword("123456");
        Object res=mq.queryValue("select t.username from t_user t  where t.username=?",new String[]{u.getUsername()});
        System.out.println(res);
    }
    public static void unionQueryTest(){
        MySqlQuery mq = new MySqlQuery();
        UserVo u = new UserVo();
        u.setId(3);
        u.setUsername("xxbb");
        u.setPassword("123456");
        List res=mq.queryRows("select t.id,t.username,t.password,t.if_freeze,p.role from t_user t left join t_purview p on p.id=t.id where t.id=? and t.username=? and t.password=?",u.getClass(),new String[]{u.getId().toString(),u.getUsername(),u.getPassword()});
        System.out.println(res);
    }
    public static void queryTest(){
        MySqlQuery mq = new MySqlQuery();
        User u = new User();
        u.setId(3);
        u.setUsername("xxbb");
        u.setPassword("123456");
        List res=mq.queryRows("select t.id,t.username,t.password from t_user t  where t.id=? and t.username=? and t.password=?",u.getClass(),new String[]{u.getId().toString(),u.getUsername(),u.getPassword()});
        System.out.println(res);
    }

}
