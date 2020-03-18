package com.xxbb.sorm.bean;

/**
 * 管理配置文件信息
 * @author xxbb
 */
public class Configuration {
    /**
     * 使用数据库类型名称:mysql
     */
    private String usingDB;
    /**
     * 数据库连接驱动
     */
    private String driverClass;
    /**
     * 连接url
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 项目绝对路径
     */
    private String srcPath;
    /**
     * 与数据库表对应的po类所在的包名（po：persistence object 持久化对象）
     */
    private String poPackage;
    /**
     * 当前使用的数据库名称
     */
    private String catalog;

    /**
     * 当前使用的查询类的路径
     */
    private String queryClass;
    public Configuration() {
    }

    public Configuration(String usingDB, String driverClass, String url, String username, String password, String srcPath, String poPackage, String catalog, String queryClass) {
        this.usingDB = usingDB;
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;
        this.srcPath = srcPath;
        this.poPackage = poPackage;
        this.catalog = catalog;
        this.queryClass = queryClass;
    }

    public String getUsingDB() {
        return usingDB;
    }

    public void setUsingDB(String usingDB) {
        this.usingDB = usingDB;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(String queryClass) {
        this.queryClass = queryClass;
    }
}
