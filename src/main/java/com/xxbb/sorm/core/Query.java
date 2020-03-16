package com.xxbb.sorm.core;

import com.sun.deploy.panel.ITreeNode;

import java.util.List;

/**
 * 负责查询，（对外提供服务的核心类）
 * @author xxbb
 */
public interface Query {
    /**
     * 直接执行一个DML语句 DML数据操纵语言
     * @param sql sql语句
     * @param params 参数
     * @return 执行sql语句影响的行数
     */
    int executeDML(String sql, Object[] params);

    /**
     * 添加一个对象到数据库中
     * @param obj 要存储的对象
     */
    void insert(Object obj);

    /**
     * 删除class表示类对应的数据库表中的记录（指定主键值id的记录）
     * @param clazz 和数据库表对应的class类
     * @param id 主键的值
     */
    void delete(Class clazz,int id);

    /**
     * 删除对象在数据库中对应的记录（对象所在的类对应数据库表，对象的主键的值对应到记录）
     * @param obj
     */
    void delete(Object obj);

    /**
     * 更新对象对应的记录，并且只更新指定的字段的值
     * @param obj 所需要更新的对象
     * @param fieldName 需要更新的属性名，对应数据库字段名
     * @return 受影响的行数
     */
    int update(Object obj,String[] fieldName);

    /**
     * 查询返回多行记录，并且将每行记录封装到clazz指定类的对象中
     * @param sql 查询语句
     * @param clazz 封装数据的javabean类的clazz对象
     * @param params sql参数
     * @return 查询到的结果
     */
    List queryRows(String sql,Class clazz,Object[] params);

    /**
     * 查询返回一行记录，并且将每行记录封装到clazz指定类的对象中
     * @param sql 查询语句
     * @param clazz 封装数据的javabean类的clazz对象
     * @param params sql参数
     * @return 查询到的结果
     */
    Object queryUniqueRows(String sql,Class clazz,Object[] params);
    /**
     * 查询返回一行一列记录，并且将每行记录封装到clazz指定类的对象中
     * @param sql 查询语句
     * @param clazz 封装数据的javabean类的clazz对象
     * @param params sql参数
     * @return 查询到的结果
     */
    Object queryValue(String sql,Class clazz,Object[] params);
    /**
     * 查询返回一行一列的数字记录，并且将每行记录封装到clazz指定类的对象中
     * @param sql 查询语句
     * @param clazz 封装数据的javabean类的clazz对象
     * @param params sql参数
     * @return 查询到的结果
     */
    Number queryNumber(String sql,Class clazz,Object[] params);
}
