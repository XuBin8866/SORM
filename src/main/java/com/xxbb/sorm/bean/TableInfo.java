package com.xxbb.sorm.bean;

import java.util.List;
import java.util.Map;

/**
 * 存储表结构信息
 * @author xxbb
 */
public class TableInfo {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 所有字段信息
     */
    private Map<String,ColumnInfo> colums;
    /**
     * 唯一主键(目前只考虑表中只有一个主键的情况)
     */
    private ColumnInfo uniquePrimaryKey;
    /**
     * 联合主键（拓展）
     */
    private List<ColumnInfo>  unionPrimaryKey;

    public TableInfo() {
    }

    public TableInfo(String tableName, Map<String, ColumnInfo> colums, ColumnInfo uniquePrimaryKey) {
        this.tableName = tableName;
        this.colums = colums;
        this.uniquePrimaryKey = uniquePrimaryKey;
    }

    public TableInfo(String tableName, Map<String, ColumnInfo> colums,List<ColumnInfo> unionPrimaryKey) {
        this.tableName = tableName;
        this.colums = colums;
        this.unionPrimaryKey = unionPrimaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, ColumnInfo> getColums() {
        return colums;
    }

    public void setColums(Map<String, ColumnInfo> colums) {
        this.colums = colums;
    }

    public ColumnInfo getUniquePrimaryKey() {
        return uniquePrimaryKey;
    }

    public void setUniquePrimaryKey(ColumnInfo uniquePrimaryKey) {
        this.uniquePrimaryKey = uniquePrimaryKey;
    }

    public List<ColumnInfo> getUnionPrimaryKey() {
        return unionPrimaryKey;
    }

    public void setUnionPrimaryKey(List<ColumnInfo> unionPrimaryKey) {
        this.unionPrimaryKey = unionPrimaryKey;
    }
}
