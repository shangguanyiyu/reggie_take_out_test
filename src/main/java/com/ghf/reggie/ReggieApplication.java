package com.ghf.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j/*打印日志 log*/
/*注解为SpringBoot的启动类*/
@SpringBootApplication
@ServletComponentScan/*扫描过滤器*/
@EnableTransactionManagement
@EnableCaching/*开启缓存注解功能*/
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动");
    }
}
