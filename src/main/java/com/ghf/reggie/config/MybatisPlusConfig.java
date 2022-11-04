package com.ghf.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

/*
* 配置MP的分页插件
*
* */

@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        /*加入一个分页拦截器*/
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        return mybatisPlusInterceptor;
    }
}
