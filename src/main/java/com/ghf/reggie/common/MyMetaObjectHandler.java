package com.ghf.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
* 自定义元数据对象处理器
*
* */
@Component/*它的作用就是实现bean的注入*/
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充Insert={}",metaObject.toString());
        /*为四个字段赋值*/
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充UpDATE={}",metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("updateUser", Thread.currentThread().getId());
        final long id = Thread.currentThread().getId();
        final String name = Thread.currentThread().getName();

        /*
         * 获取当前线程的id
         * */
        log.info("当前线程id={},name={}",id,name);
    }
}
