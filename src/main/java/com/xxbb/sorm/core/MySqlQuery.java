package com.xxbb.sorm.core;

import com.xxbb.po.User;
import com.xxbb.sorm.bean.ColumnInfo;
import com.xxbb.sorm.bean.TableInfo;
import com.xxbb.sorm.util.JDBCUtils;
import com.xxbb.sorm.util.ReflectUtils;
import com.xxbb.sorm.util.StringUtils;
import com.xxbb.vo.UserVo;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责MySql数据库的操作
 * @author xxbb
 */
public class MySqlQuery implements Query {
    public static void main(String[] args) {
       queryTest();
    }
    public static void unionQueryTest(){
        MySqlQuery mq = new MySqlQuery();
        UserVo u = new UserVo();
        u.setId(3);
        u.setUsername("xxbb");
        u.setPassword("123456");
        List res=mq.queryRows("select t.id,t.username,t.password,p.role from t_user t left join t_purview p on p.id=t.id where t.id=? and t.username=? and t.password=?",u.getClass(),new String[]{u.getId().toString(),u.getUsername(),u.getPassword()});
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


    @Override
    public int executeDML(String sql, Object[] params) {
        //记录受影响的行数
        int count=0;
        //获取连接
        Connection conn=DBManager.getConnection();
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement(sql);
            //给sql传递参数
            JDBCUtils.handleParams(ps,params);
            count=ps.executeUpdate();
            System.out.println("受影响的行数："+count);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void insert(Object obj) {
        //获取信息
        Class clazz=obj.getClass();
        //存储object对象的非空属性
        List<Object> params=new ArrayList<>();
        //获取表信息
        TableInfo tableInfo=TableContext.getPoClassTableMap().get(clazz);
        //构建sql语句
        StringBuilder sql=new StringBuilder();
        sql.append("insert into ").append(tableInfo.getTableName()).append("(");
        //获得属性
        Field[] fields=clazz.getDeclaredFields();

        //遍历属性
        for(Field f:fields){
            String fieldName=f.getName();
            Object fieldValue=ReflectUtils.invokeGet(obj,StringUtils.humpToLine(fieldName));
            if(fieldValue!=null){
                sql.append(StringUtils.humpToLine(fieldName)).append(",");
                params.add(fieldValue);
            }
        }
        //替换最后一个逗号
        sql.setCharAt(sql.length()-1,')');
        sql.append(" values(");
        for(int i=0;i<params.size();i++){
            sql.append("?,");
        }
        //替换最后一个逗号
        sql.setCharAt(sql.length()-1,')');

        //执行sql
       executeDML(sql.toString(),params.toArray());


    }

    @Override
    public void delete(Class clazz, Object id) {
        if(id==null){
            throw new RuntimeException("传入的删除条件为空，执行删除功能失败！");
        }
        //获取表信息
        TableInfo tableInfo=TableContext.getPoClassTableMap().get(clazz);
        //获取主键
        ColumnInfo uniquePrimaryKey= tableInfo.getUniquePrimaryKey();
        //生成语句
        StringBuilder sql=new StringBuilder();
        sql.append("delete from ").append(tableInfo.getTableName()).append(" where ").append(uniquePrimaryKey.getName()).append("=? ");
        //执行语句
        int count=executeDML(sql.toString(),new Object[]{id});
    }

    @Override
    public void delete(Object obj) {
        //获取Class对象
        Class clazz=obj.getClass();
        //获取表信息
        TableInfo tableInfo=TableContext.getPoClassTableMap().get(clazz);
        System.out.println(tableInfo);
        //获取主键
        ColumnInfo uniquePrimaryKey= tableInfo.getUniquePrimaryKey();
        //通过反射获取传入对象的主键值
        Object uniquePrimaryKeyValue= ReflectUtils.invokeGet(obj,uniquePrimaryKey.getName());
        //执行语句
        delete(clazz,uniquePrimaryKeyValue);

    }

    @Override
    public int update(Object obj, String[] fieldName) {
        //获取信息
        Class clazz=obj.getClass();
        //存储object对象的非空属性
        List<Object> params=new ArrayList<>();
        //获取表信息
        TableInfo tableInfo=TableContext.getPoClassTableMap().get(clazz);
        //获取主键
        ColumnInfo uniquePrimaryKey=tableInfo.getUniquePrimaryKey();
        //构建sql语句
        StringBuilder sql=new StringBuilder();
        sql.append("update ").append(tableInfo.getTableName()).append(" set ");
        for(String name:fieldName){
            Object value=ReflectUtils.invokeGet(obj,StringUtils.humpToLine(name));
            sql.append(name).append("=?,");
            params.add(value);
        }
        sql.setCharAt(sql.length()-1,' ');
        sql.append("where ").append(uniquePrimaryKey.getName()).append("=?");
        //获取修改的查询条件值
        Object queryValue=ReflectUtils.invokeGet(obj,uniquePrimaryKey.getName());
        params.add(queryValue);

        return executeDML(sql.toString(),params.toArray());
    }

    @Override
    public int update(Object obj) {
        //获取信息
        Class clazz=obj.getClass();
        //存储object对象的需要修改的属性
        List<Object> params=new ArrayList<>();
        //获取表信息
        TableInfo tableInfo=TableContext.getPoClassTableMap().get(clazz);
        //获取主键
        ColumnInfo uniquePrimaryKey=tableInfo.getUniquePrimaryKey();
        //获取查询条件名称，即主键的属性名
        String key=StringUtils.lineToHump(uniquePrimaryKey.getName());
        //构建sql语句
        StringBuilder sql=new StringBuilder();
        sql.append("update ").append(tableInfo.getTableName()).append(" set ");
        //获取需要更新的属性
        Field[] fields= clazz.getDeclaredFields();
        for(Field f:fields){
            String fieldName=f.getName();
            Object fieldValue=ReflectUtils.invokeGet(obj,StringUtils.humpToLine(fieldName));
            //属性值非空且不是主键
            if((!key.equals(fieldName))&&fieldValue!=null){
                sql.append(fieldName).append("=?,");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length()-1,' ');
        sql.append("where ").append(uniquePrimaryKey.getName()).append("=?");
        params.add(ReflectUtils.invokeGet(obj,uniquePrimaryKey.getName()));

        return executeDML(sql.toString(),params.toArray());
    }

    @Override
    public List queryRows(String sql, Class clazz, Object[] params) {
        Connection conn=DBManager.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        //存放查询结果
        List<Object> res=new ArrayList<>();
        try{
            //执行查询操作
            ps=conn.prepareStatement(sql);
            JDBCUtils.handleParams(ps,params);
            rs=ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData resultSetMetaData=rs.getMetaData();
            //遍历查询结果
            while(rs.next()){
                //实例化查询结果对应的类
                Object rowObject=clazz.newInstance();

                //获取查询结果的列数据
                for(int i=0;i<resultSetMetaData.getColumnCount();i++){
                    //获取列名，从1开始
                    String columnName=resultSetMetaData.getColumnLabel(i+1);
                    Object columnValue=rs.getObject(i+1);

                    //将数据赋值给类对象
                    ReflectUtils.invokeSet(rowObject,columnName,columnValue);
                }
                res.add(rowObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Object queryUniqueRows(String sql, Class clazz, Object[] params) {
        List res=queryRows(sql,clazz,params);
        return (res!=null&&res.size()>0)?res.get(0):null;
    }

    @Override
    public Object queryValue(String sql, Object[] params) {
        Connection conn=DBManager.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        //存放查询结果,一行一列一个值
        Object res=new Object();
        try{
            //执行查询操作
            ps=conn.prepareStatement(sql);
            JDBCUtils.handleParams(ps,params);
            rs=ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData resultSetMetaData=rs.getMetaData();
            //遍历查询结果
            while(rs.next()){
                res=rs.getObject(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Number queryNumber(String sql, Object[] params) {
        return (Number) queryValue(sql,params);
    }
}
