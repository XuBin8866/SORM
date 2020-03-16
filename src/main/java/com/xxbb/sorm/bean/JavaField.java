package com.xxbb.sorm.bean;

/**
 * 封装了java属性的get，set方法
 * @author xxbb
 */
public class JavaField {
    /**
     * 属性的源码信息：如 private int id
     */
    private String fieldInfo;
    /**
     * get方法的源码信息，如 public int getId()
     */
    private String getInfo;
    /**
     * set方法的源码信息，如 public void setId()
     */
    private String setInfo;

    public JavaField() {
    }

    public JavaField(String fieldInfo, String getInfo, String setInfo) {
        this.fieldInfo = fieldInfo;
        this.getInfo = getInfo;
        this.setInfo = setInfo;
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }

    @Override
    public String toString() {
        System.out.println(fieldInfo);
        System.out.println(getInfo);
        System.out.println(setInfo);
        return super.toString();
    }
}

