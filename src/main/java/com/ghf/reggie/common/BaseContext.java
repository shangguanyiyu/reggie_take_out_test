package com.ghf.reggie.common;
/*
* 基于ThreadLocal的工具类，用于保存当前登录用户的id,作用域是线程
* */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
       return threadLocal.get();
    }
}
