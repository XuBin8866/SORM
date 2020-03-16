package com.xxbb.sorm.core;

/**
 *  mysql数据类型和java数据类型的转换
 * @author xxbb
 */
public class MySqlTypeConvertor implements TypeConvertor {
    /**
     * 数据库中各个字段类型名称
     */
    private static final String VARCHAR="varchar";
    private static final String INT="int";
    private static final String TINY_INT="tinyint";
    private static final String SMALL_INT="smallint";
    private static final String MEDIUM_INT="mediumint";
    private static final String INTEGER="integer";
    private static final String BIG_INT="bigint";
    private static final String DOUBLE="double";
    private static final String FLOAT="float";
    private static final String BLOB="blob";
    private static final String CLOB="clob";
    private static final String DATE="date";
    private static final String TIME="time";
    private static final String TIME_STAMP="timestamp";

    @Override
    public String databaseTypeToJavaType(String columnType) {
        //varchar-->String
        if(VARCHAR.equals(columnType)){
            return "String";
        }
        if(INT.equals(columnType)||
                TINY_INT.equals(columnType)||
                SMALL_INT.equals(columnType)||
                MEDIUM_INT.equals(columnType)||
                INTEGER.equals(columnType)){
            return "Integer";
        }
        if(BIG_INT.equals(columnType)){
            return "Long";
        }
        if(DOUBLE.equals(columnType)){
            return "Double";
        }
        if(FLOAT.equals(columnType)){
            return "Float";
        }
        if(BLOB.equals(columnType)){
            return "java.sql.Blob";
        }
        if(CLOB.equals(columnType)){
            return "java.sql.Clob";
        }
        if(DATE.equals(columnType)){
            return "java.sql.Date";
        }
        if(TIME.equals(columnType)){
            return "java.sql.Time";
        }
        if(TIME_STAMP.equals(columnType)){
            return "java.sql.Timestamp";
        }
        return "";
    }

    @Override
    public String javaTypeToDatabaseType(String javaType) {
        return null;
    }
}
