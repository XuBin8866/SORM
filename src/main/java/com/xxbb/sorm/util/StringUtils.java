package com.xxbb.sorm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 封装了字符串常用的操作
 * @author xxbb
 */
public class StringUtils {
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");



    /**
     * 将传入的表名去除t_字符，转化为类名
     * @param str 传入字段
     * @return 取出t_的下划线转驼峰+首字母大写字段
     */
    public static String tableNameToClassName(String str){
        return firstCharToUpperCase(lineToHump(str.substring(2)));
    }

    /**
     * 将类名转化为数据库表名
     * @param str 传入类名
     * @return 数据库表名
     */
    public static String classNameToTableName(String str){
        return "t_"+humpToLine(str);
    }

    /**
     * 将数据库字段名转化为类的命名规则，即下划线改驼峰+首字母大写，例如要获取if_freeze字段的方法，方法为getIfFreeze(),
     * @param str 传入字段
     * @return  下划线改驼峰+首字母大写
     */
    public static String columnNameToMethodName(String str){
        return firstCharToUpperCase(lineToHump(str));
    }

    /**
     * 将传入字符串的首字母大写
     * @param str 传入字符串
     * @return 首字母大写的字符串
     */
    public  static String firstCharToUpperCase(String str){
        return str.toUpperCase().substring(0,1)+str.substring(1);
    }
    /**
     * 下划线转驼峰
     * @param str 待转换字符串
     * @return  驼峰风格字符串
     */
    public static String lineToHump(String str) {
        //将小写转换
        String newStr=str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(newStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     * @param str 待转换字符串
     * @return  下划线风格字符串
     */
    public static String humpToLine(String str) {
        //将首字母先进行小写转换
        String newStr=str.substring(0,1).toLowerCase()+str.substring(1);
        //比对字符串中的大写字符
        Matcher matcher = humpPattern.matcher(newStr);
        StringBuffer sb = new StringBuffer();
        //匹配替换
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        String str=columnNameToMethodName(humpToLine("ifFreeze"));
        System.out.println(str);
    }

}
