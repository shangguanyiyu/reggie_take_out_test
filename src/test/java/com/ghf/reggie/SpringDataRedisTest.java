package com.ghf.reggie;

import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {
    @Resource/*jdk提供注解*/
    private RedisTemplate redisTemplate;

    @Test
    public void testString(){
//        System.out.println("122112");
        ValueOperations valueOperations = redisTemplate.opsForValue();
//        valueOperations.set("city123","beijing");
//        final Object city = valueOperations.get("city123");
        redisTemplate.opsForValue().set("code1","12345", 10l);
//        System.out.println(city);
    }
}
