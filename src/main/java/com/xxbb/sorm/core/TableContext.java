package com.xxbb.sorm.core;

import com.xxbb.sorm.bean.ColumnInfo;
import com.xxbb.sorm.bean.Configuration;
import com.xxbb.sorm.bean.TableInfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责管理数据库的所有表结构和类结构的关系，并可以根据表结构生成类结构
 * @author xxbb
 */
public class TableContext {
    /**
     * 表名为key，表信息对象为value
     */
    public  static Map<String, TableInfo> databaseTableMap=new HashMap<>();
    /**
     * 将po的class对象和表信息对象关联起来，便于重用！
     */
    private static Map<Class,TableInfo> poClassTableMap=new HashMap<>();
    private TableContext(){

    }
    static{
        try {
            //初始化获得表的信息
            Connection conn=DBManager.getConnection();
            //获取数据库元数据
            DatabaseMetaData databaseMetaData=conn.getMetaData();
            //获取使用的数据库名称
            String catalog= DBManager.getConfiguration().getCatalog();
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
            ResultSet tableResultSet=databaseMetaData.getTables(catalog,"%","%",new String[]{"TABLE"});
            while(tableResultSet.next()){
                String tableName=(String) tableResultSet.getObject("TABLE_NAME");
                TableInfo tableInfo=new TableInfo(tableName, new HashMap<>(), new ArrayList<>());
                databaseTableMap.put(tableName,tableInfo);
                ResultSet columnResultSet=databaseMetaData.getColumns(catalog,"%",tableName,null);
                while(columnResultSet.next()){
                    ColumnInfo columnInfo=new ColumnInfo(columnResultSet.getString("COLUMN_NAME"),
                            columnResultSet.getString("TYPE_NAME"),
                            0);
                    tableInfo.getColums().put(columnResultSet.getString("COLUMN_NAME"),columnInfo);
                }

                //获取主键
                ResultSet primaryKeyResultSet=databaseMetaData.getPrimaryKeys(catalog,"%",tableName);

                while (primaryKeyResultSet.next()){
                    //设置主键
                    ColumnInfo columnInfo2=(ColumnInfo)tableInfo.getColums().get(primaryKeyResultSet.getString("COLUMN_NAME"));
                    columnInfo2.setKeyType(1);
                    tableInfo.getUnionPrimaryKey().add(columnInfo2);
                }
                //如果主键里只有一个候选码，说明该表只有唯一主键
                if(tableInfo.getUnionPrimaryKey().size()==1){
                    tableInfo.setUniquePrimaryKey(tableInfo.getUnionPrimaryKey().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<String,TableInfo> tables=TableContext.databaseTableMap;
    }
}
