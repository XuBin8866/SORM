package com.xxbb.sorm.util;

import java.lang.reflect.Method;

/**
 * 封装反射常用操作
 * @author xxbb
 */
public class ReflectUtils {
    /**
     * 调用Object对应属性的get方法
     * @param obj 类对象
     * @param columnName 与类对象属性对应的数据库字段名
     * @return 类对象的属性
     */
    public static Object invokeGet(Object obj,String columnName){
        Class clazz=obj.getClass();
        Method method= null;
        Object res=null;
        try {
            method = clazz.getDeclaredMethod("get"+ StringUtils.columnNameToMethodName(columnName), (Class<?>) null);
            res=method.invoke(obj, (Object) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * 调用Object对应属性的get方法
     * @param obj 类对象
     * @param columnName 与类对象属性对应的数据库字段名
     * @param value 属性值
     */
    public static void invokeSet(Object obj,String columnName,Object value){
        Class clazz=obj.getClass();
        Method method= null;
        try {
            method = clazz.getDeclaredMethod("set"+ StringUtils.columnNameToMethodName(columnName),value.getClass());
            method.invoke(obj,value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

