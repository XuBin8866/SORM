package com.xxbb.sorm.core;

/**
 * 查询工厂，调用需要的查询类
 * @author xxbb
 */
public class QueryFactory {
    /**
     * 查询类的原型对象
     */
    private static BaseQuery prototypeObj;
    /**
     * 静态内部类实现单例，只有在使用静态内部类时才会加载
     * 天然线程安全，延时加载
     */
    private static class Instance{
        private static QueryFactory instance=new QueryFactory();
    }

    //读取配置文件中的查询类
    static {
        try {
            Class clazz = Class.forName(DBManager.getConfiguration().getQueryClass());
            prototypeObj = (BaseQuery) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private QueryFactory(){

    }

    /**
     * 获取单例工厂
     * @return 工厂对象
     */
    public static QueryFactory getInstance(){
        return Instance.instance;
    }
    /**
     * 获取查询类
     * @return 查询类
     */
    public BaseQuery createQuery(){
        try {
            return (BaseQuery) prototypeObj.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }


}
