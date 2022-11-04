package com.ghf.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/*
*基于代理，代理controller，通过AOP将控制层的方法进行拦截，若是抛异常，则在这个地方进行处理
* */
@ControllerAdvice(annotations = {RestController.class, Controller.class})/*通知类型的注解，指定拦截哪些controler*/
@ResponseBody/*因为要返回JSON数据*/
@Slf4j/*sleaf4j*/
public class GlobalExceptionHandle {
    /*异常处理的注解*/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String > exceptionHandle(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            final String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return  R.error(msg);
        }
        return  R.error("未知错误");/*给前端写*/
    }


    /*异常处理的注解*/
    @ExceptionHandler(CustomException.class)
    public R<String > exceptionHandle(CustomException ex){
        log.error(ex.getMessage());

        return  R.error(ex.getMessage());/*给前端写*/
    }
}
