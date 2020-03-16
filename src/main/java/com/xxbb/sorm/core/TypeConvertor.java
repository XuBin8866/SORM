package com.xxbb.sorm.core;

/**
 * 负责java数据类型和数据库数据类型的相互转换
 * @author xxbb
 */
public interface TypeConvertor {
    /**
     * 将数据库类型转化为java的数据类型
     * @param columnType 数据库字段的数据类型
     * @return java属性的数据类型
     */
    String databaseTypeToJavaType(String columnType);

    /**
     * 将java的数据类型转化为数据库的数据类型
     * @param javaType java属性的数据类型
     * @return 数据库字段的数据类型
     */
    String javaTypeToDatabaseType(String javaType);
}
