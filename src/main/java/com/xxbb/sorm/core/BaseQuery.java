package com.xxbb.sorm.core;

import com.xxbb.sorm.bean.ColumnInfo;
import com.xxbb.sorm.bean.TableInfo;
import com.xxbb.sorm.util.JDBCUtils;
import com.xxbb.sorm.util.ReflectUtils;
import com.xxbb.sorm.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责查询，（对外提供服务的核心类）
 *
 * @author xxbb
 */
public abstract class BaseQuery implements Cloneable{


    public Object executeQueryTemplate(String sql, Class clazz, Object[] params, CallBack back) {
        Connection conn = DBManager.getConnection();
        System.out.println("连接："+conn);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //执行查询操作
            ps = conn.prepareStatement(sql);
            JDBCUtils.handleParams(ps, params);
            rs = ps.executeQuery();
            //相当于一个占位符，说明在调用模板时这里会执行重写的doExecute方法
            // 即具体内容到具体方法再去写
            return back.doExecute(conn, ps, rs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DBManager.closeAll(conn, ps, rs);
        }
    }

    /**
     * 直接执行一个DML语句 DML数据操纵语言
     *
     * @param sql    sql语句
     * @param params 参数
     * @return 执行sql语句影响的行数
     */
    public int executeDML(String sql, Object[] params) {
        //记录受影响的行数
        int count = 0;
        //获取连接
        Connection conn = DBManager.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            //给sql传递参数
            JDBCUtils.handleParams(ps, params);
            count = ps.executeUpdate();
            System.out.println("受影响的行数：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.closeAll(conn, ps, null);
        }

        return count;
    }

    /**
     * 添加一个对象到数据库中
     * 把对象中不为空的属性存储到数据库中
     *
     * @param obj 要存储的对象
     */
    public void insert(Object obj) {
        //获取信息
        Class clazz = obj.getClass();
        //存储object对象的非空属性
        List<Object> params = new ArrayList<>();
        //获取表信息
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);
        //构建sql语句
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableInfo.getTableName()).append("(");
        //获得属性
        Field[] fields = clazz.getDeclaredFields();

        //遍历属性
        for (Field f : fields) {
            String fieldName = f.getName();
            Object fieldValue = ReflectUtils.invokeGet(obj, StringUtils.humpToLine(fieldName));
            if (fieldValue != null) {
                sql.append(StringUtils.humpToLine(fieldName)).append(",");
                params.add(fieldValue);
            }
        }
        //替换最后一个逗号
        sql.setCharAt(sql.length() - 1, ')');
        sql.append(" values(");
        for (int i = 0; i < params.size(); i++) {
            sql.append("?,");
        }
        //替换最后一个逗号
        sql.setCharAt(sql.length() - 1, ')');

        //执行sql
        executeDML(sql.toString(), params.toArray());
    }

    /**
     * 删除class表示类对应的数据库表中的记录（指定主键值id的记录）
     *
     * @param clazz 和数据库表对应的class类
     * @param id    主键的值
     */
    public void delete(Class clazz, Object id) {
        if (id == null) {
            throw new RuntimeException("传入的删除条件为空，执行删除功能失败！");
        }
        //获取表信息
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);
        //获取主键
        ColumnInfo uniquePrimaryKey = tableInfo.getUniquePrimaryKey();
        //生成语句
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(tableInfo.getTableName()).append(" where ").append(uniquePrimaryKey.getName()).append("=? ");
        //执行语句
        int count = executeDML(sql.toString(), new Object[]{id});
    }


    /**
     * 删除对象在数据库中对应的记录（对象所在的类对应数据库表，对象的主键的值对应到记录）
     *
     * @param obj
     */
    public void delete(Object obj) {
        //获取Class对象
        Class clazz = obj.getClass();
        //获取表信息
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);
        //获取主键
        ColumnInfo uniquePrimaryKey = tableInfo.getUniquePrimaryKey();
        //通过反射获取传入对象的主键值
        Object uniquePrimaryKeyValue = ReflectUtils.invokeGet(obj, uniquePrimaryKey.getName());
        //执行语句
        delete(clazz, uniquePrimaryKeyValue);

    }


    /**
     * 更新对象对应的记录，并且只更新指定的字段的值
     *
     * @param obj       所需要更新的对象
     * @param fieldName 需要更新的属性名，对应数据库字段名
     * @return 受影响的行数
     */
    public int update(Object obj, String[] fieldName) {
        //获取信息
        Class clazz = obj.getClass();
        //存储object对象的非空属性
        List<Object> params = new ArrayList<>();
        //获取表信息
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);
        //获取主键
        ColumnInfo uniquePrimaryKey = tableInfo.getUniquePrimaryKey();
        //构建sql语句
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tableInfo.getTableName()).append(" set ");
        for (String name : fieldName) {
            Object value = ReflectUtils.invokeGet(obj, StringUtils.humpToLine(name));
            sql.append(name).append("=?,");
            params.add(value);
        }
        sql.setCharAt(sql.length() - 1, ' ');
        sql.append("where ").append(uniquePrimaryKey.getName()).append("=?");
        //获取修改的查询条件值
        Object queryValue = ReflectUtils.invokeGet(obj, uniquePrimaryKey.getName());
        params.add(queryValue);

        return executeDML(sql.toString(), params.toArray());
    }


    /**
     * 更新对象对应的记录,不指定更新字段，条件
     *
     * @param obj
     * @return
     */
    public int update(Object obj) {
        //获取信息
        Class clazz = obj.getClass();
        //存储object对象的需要修改的属性
        List<Object> params = new ArrayList<>();
        //获取表信息
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);
        //获取主键
        ColumnInfo uniquePrimaryKey = tableInfo.getUniquePrimaryKey();
        //获取查询条件名称，即主键的属性名
        String key = StringUtils.lineToHump(uniquePrimaryKey.getName());
        //构建sql语句
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tableInfo.getTableName()).append(" set ");
        //获取需要更新的属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            String fieldName = f.getName();
            Object fieldValue = ReflectUtils.invokeGet(obj, StringUtils.humpToLine(fieldName));
            //属性值非空且不是主键
            if ((!key.equals(fieldName)) && fieldValue != null) {
                sql.append(fieldName).append("=?,");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length() - 1, ' ');
        sql.append("where ").append(uniquePrimaryKey.getName()).append("=?");
        params.add(ReflectUtils.invokeGet(obj, uniquePrimaryKey.getName()));

        return executeDML(sql.toString(), params.toArray());
    }

    /**
     * 查询返回多行记录，并且将每行记录封装到clazz指定类的对象中
     *
     * @param sql    查询语句
     * @param clazz  封装数据的javabean类的clazz对象
     * @param params sql参数
     * @return 查询到的结果
     */
    public List queryRows(String sql, Class clazz, Object[] params) {
        return (List) executeQueryTemplate(sql, clazz, params, (connection, ps, rs) -> {
            //存放查询结果
            List<Object> res = new ArrayList<>();
            //获取结果集的元数据
            ResultSetMetaData resultSetMetaData = null;
            try {
                resultSetMetaData = rs.getMetaData();
                //遍历查询结果
                while (rs.next()) {
                    //实例化查询结果对应的类
                    Object rowObject = clazz.newInstance();

                    //获取查询结果的列数据
                    for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                        //获取列名，从1开始
                        String columnName = resultSetMetaData.getColumnLabel(i + 1);
                        Object columnValue = rs.getObject(i + 1);

                        //将数据赋值给类对象
                        ReflectUtils.invokeSet(rowObject, columnName, columnValue);
                    }
                    res.add(rowObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        });
    }

    /**
     * 查询返回一行记录，并且将每行记录封装到clazz指定类的对象中
     *
     * @param sql    查询语句
     * @param clazz  封装数据的javabean类的clazz对象
     * @param params sql参数
     * @return 查询到的结果
     */
    public Object queryUniqueRows(String sql, Class clazz, Object[] params) {
        List res = queryRows(sql, clazz, params);
        return (res != null && res.size() > 0) ? res.get(0) : null;
    }

    /**
     * 查询返回一行一列记录，并且将每行记录封装到clazz指定类的对象中
     *
     * @param sql    查询语句
     * @param params sql参数
     * @return 查询到的结果
     */
    public Object queryValue(String sql, Object[] params) {
        return executeQueryTemplate(sql, null, params, (conn, ps, rs) -> {
            //获取查询结果，虽然这里用了while，但正确使用这个方法只会返回一行一列的值
            Object res = new Object();
            try {
                while (rs.next()) {

                    res = rs.getObject(1);
                }
                } catch(SQLException ex){
                    ex.printStackTrace();
                }
                return res;
        });
}

    /**
     * 查询返回一行一列的数字记录，并且将每行记录封装到clazz指定类的对象中
     *
     * @param sql    查询语句
     * @param params sql参数
     * @return 查询到的结果
     */
    public Number queryNumber(String sql, Object[] params) {
        return (Number) queryValue(sql, params);
    }

    /**
     * 实现克隆方法
     * @return 克隆的实例对象
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
