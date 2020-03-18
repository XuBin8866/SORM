package com.xxbb.sorm.util;

import com.xxbb.sorm.bean.ColumnInfo;
import com.xxbb.sorm.bean.JavaField;
import com.xxbb.sorm.bean.TableInfo;
import com.xxbb.sorm.core.DBManager;
import com.xxbb.sorm.core.MySqlTypeConvertor;
import com.xxbb.sorm.core.TableContext;
import com.xxbb.sorm.core.TypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了生成java文件的操作
 * @author xxbb
 */
public class JavaFileUtils {

    public static void createJavaPoFile(TableInfo tableInfo,TypeConvertor typeConvertor){
        String src=createJavaSrc(tableInfo,typeConvertor);
        String srcPath=DBManager.getConfiguration().getSrcPath();
        String packagePath=DBManager.getConfiguration().getPoPackage().replaceAll("\\.","\\\\");
        File poFile=new File(srcPath+"/"+packagePath);
        if(!poFile.exists()){
            Boolean flag=poFile.mkdirs();
        }

        BufferedWriter bufferedWriter=null;
        try {
            File po=new File(srcPath+"/"+packagePath+"/"+
                    StringUtils.tableNameToClassName(tableInfo.getTableName())+".java");
            bufferedWriter=new BufferedWriter(new FileWriter(po));
            bufferedWriter.write(src);
            bufferedWriter.close();
            System.out.println("更新了数据库表 "+tableInfo.getTableName()+"的对应类："+StringUtils.tableNameToClassName(tableInfo.getTableName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据表信息生成java类的源代码
     * @param tableInfo 表信息
     * @param typeConvertor 类型转化器
     * @return java类源代码
     */
    public static String createJavaSrc(TableInfo tableInfo,TypeConvertor typeConvertor){
        //获取所有字段
        Map<String,ColumnInfo> columnInfoMap=tableInfo.getColums();
        //将所有字段转化为javaField的数组
        List<JavaField> javaFieldList=new ArrayList<>();
        for(ColumnInfo c:columnInfoMap.values()){
            javaFieldList.add(createJavaField(c,typeConvertor));
        }
        //构建源文件
        StringBuilder srcStr=new StringBuilder();
        //生成package语句
        srcStr.append("package ").append(DBManager.getConfiguration().getPoPackage()).append(";\n\n");
        //生成import语句
        srcStr.append("import java.sql.*;\n");
        srcStr.append("import java.util.*;\n\n");
        //生成类声明
        srcStr.append("public class ").append(StringUtils.tableNameToClassName(tableInfo.getTableName())).append("{\n\n");
        //生成属性列表
        for(JavaField j:javaFieldList){
            srcStr.append(j.getFieldInfo());
        }
        srcStr.append("\n");
        //生成构造方法
        srcStr.append("\tpublic ").append(StringUtils.tableNameToClassName(tableInfo.getTableName())).append("(){}\n");
        srcStr.append("\n");
        //生成get方法
        for(JavaField j:javaFieldList){
            srcStr.append(j.getGetInfo());
        }
        srcStr.append("\n");
        //生成set方法
        for(JavaField j:javaFieldList){
            srcStr.append(j.getSetInfo());
        }
        srcStr.append("\n");
        srcStr.append("}");

        return srcStr.toString();


    }

    /**
     * 根据字段信息生成java属性信息，如varchar username——>private String username以及它的get，set方法
     * @param columnInfo 字段信息
     * @param typeConvertor 类型转化器
     * @return java属性和get，set方法
     */
    public static JavaField createJavaField(ColumnInfo columnInfo, TypeConvertor typeConvertor){
        JavaField javaField=new JavaField();
        //获取字段类型有的时候是值是大写的，故在末尾添加toLowerCase()方法
        String javaFieldType=typeConvertor.databaseTypeToJavaType(columnInfo.getDataType().toLowerCase());

        //构建属性
        javaField.setFieldInfo("\tprivate "+javaFieldType+" "+StringUtils.lineToHump(columnInfo.getName())+";\n");
        //构建get方法
        StringBuilder getStr=new StringBuilder();
        getStr.append("\tpublic ").append(javaFieldType).append(" get").append(StringUtils.columnNameToMethodName(columnInfo.getName())).append("(){\n");
        getStr.append("\t\treturn ").append(StringUtils.lineToHump(columnInfo.getName())).append(";\n");
        getStr.append("\t}\n");
        javaField.setGetInfo(getStr.toString());
        //构建set方法
        StringBuilder setStr=new StringBuilder();
        setStr.append("\tpublic void").append(" set").append(StringUtils.columnNameToMethodName(columnInfo.getName()));
        setStr.append("(").append(javaFieldType).append(" ").append(StringUtils.lineToHump(columnInfo.getName())).append("){\n");
        setStr.append("\t\tthis.").append(StringUtils.lineToHump(columnInfo.getName())).append("=").append(StringUtils.lineToHump(columnInfo.getName())).append(";\n");
        setStr.append("\t}\n");
        javaField.setSetInfo(setStr.toString());

        return javaField;
    }
    public static void main(String[] args) {
        Map<String,TableInfo> map= TableContext.getDatabaseTableMap();
        TableInfo t=map.get("t_user");
        createJavaPoFile(t,new MySqlTypeConvertor());
    }

}

