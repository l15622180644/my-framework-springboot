package com.zt.myframeworkspringboot;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

//@SpringBootTest(classes = MyFrameworkSpringbootApplication.class)
public class Test {

    @Resource
    private RedisTemplate redisTemplate;

    @org.junit.jupiter.api.Test
    public void show(){

        System.out.println(1);
    }
}
