package com.xxbb.sorm.core;

import com.xxbb.sorm.bean.ColumnInfo;
import com.xxbb.sorm.bean.Configuration;
import com.xxbb.sorm.bean.TableInfo;
import com.xxbb.sorm.util.JavaFileUtils;
import com.xxbb.sorm.util.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责管理数据库的所有表结构和类结构的关系，并可以根据表结构生成类结构
 *
 * @author xxbb
 */
public class TableContext {
    /**
     * 表名为key，表信息对象为value
     */
    private static Map<String, TableInfo> databaseTableMap = new HashMap<>();
    /**
     * 将po的class对象和表信息对象关联起来，便于重用！
     */
    private static Map<Class, TableInfo> poClassTableMap = new HashMap<>();

    private TableContext() {

    }

    static {
        Connection conn=null;
        ResultSet tableResultSet=null;
        ResultSet primaryKeyResultSet=null;
        try {
            //初始化获得表的信息
            conn = DBManager.getConnection();
            //获取数据库元数据
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            //获取使用的数据库名称
            String catalog = DBManager.getConfiguration().getCatalog();
            /*
             * catalog -目录名称即数据库名；必须匹配的目录名称，因为它是存储在数据库中；
             *          “检索那些没有目录； null意味着目录名称不应该被用来缩小搜索范围
             *
             * schemaPattern -模式名称模式；必须匹配的模式名称是存储在数据库中；
             *                “检索那些没有模式； null意味着架构名称不应该被用来缩小搜索范围
             * tableNamePattern -表名模式；
             *                  必须匹配的表名是存储在数据库中
             * columnNamePattern -列名称模式；
             *                    必须匹配的列名称，因为它是存储在数据库中
             */
            tableResultSet = databaseMetaData.getTables(catalog, "%", "%", new String[]{"TABLE"});
            while (tableResultSet.next()) {
                String tableName = (String) tableResultSet.getObject("TABLE_NAME");
                TableInfo tableInfo = new TableInfo(tableName, new HashMap<>(), new ArrayList<>());
                databaseTableMap.put(tableName, tableInfo);
                ResultSet columnResultSet = databaseMetaData.getColumns(catalog, "%", tableName, null);
                while (columnResultSet.next()) {
                    ColumnInfo columnInfo = new ColumnInfo(columnResultSet.getString("COLUMN_NAME"),
                            columnResultSet.getString("TYPE_NAME"),
                            0);
                    tableInfo.getColums().put(columnResultSet.getString("COLUMN_NAME"), columnInfo);
                }

                //获取主键
                primaryKeyResultSet = databaseMetaData.getPrimaryKeys(catalog, "%", tableName);

                while (primaryKeyResultSet.next()) {
                    //设置主键
                    ColumnInfo columnInfo2 = (ColumnInfo) tableInfo.getColums().get(primaryKeyResultSet.getString("COLUMN_NAME"));
                    columnInfo2.setKeyType(1);
                    tableInfo.getUnionPrimaryKey().add(columnInfo2);
                }
                //如果主键里只有一个候选码，说明该表只有唯一主键
                if (tableInfo.getUnionPrimaryKey().size() == 1) {
                    tableInfo.setUniquePrimaryKey(tableInfo.getUnionPrimaryKey().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBManager.closeResultSet(primaryKeyResultSet);
            DBManager.closeResultSet(tableResultSet);
            DBManager.closeConnection(conn);
        }

        //更新类结构
        updateJavaPoFile();
        //第一次启动程序，第一次生成java文件后，准备将po类与数据库表建立映射关系，提示类找不到
        //经测试java文件已经生成了采用sleep方法无效
        //TODO 尚未解决项目第一次加载时第一次执行updateJavaPoFile()生成类文件后，无法建立映射关系的问题=
        setPoClassTableMap();

    }

    /**
     * 根据数据库的表结构生成对应的类文件
     */
    public synchronized static void updateJavaPoFile() {
        for (TableInfo t : databaseTableMap.values()) {
            JavaFileUtils.createJavaPoFile(t, new MySqlTypeConvertor());
        }
    }

    /**
     * 设置po类与数据库表的对应关系
     */
    public static void setPoClassTableMap() {
        try {
            for (TableInfo t : databaseTableMap.values()) {
                Class clazz = Class.forName("com.xxbb.po." + StringUtils.tableNameToClassName(t.getTableName()));
                poClassTableMap.put(clazz,t);

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取设置po类与数据库表的对应关系的表
     * @return 对应关系的哈希表
     */
    public static Map<Class, TableInfo> getPoClassTableMap() {
        return poClassTableMap;
    }

    /**
     * 获取数据库表
     * @return 数据库表的哈希表
     */
    public static Map<String, TableInfo> getDatabaseTableMap() {
        return databaseTableMap;
    }


    public static void main(String[] args) {
        Map<String, TableInfo> tables = TableContext.databaseTableMap;
    }
}
