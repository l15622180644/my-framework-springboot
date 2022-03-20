package com.zt.myframeworkspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zt"})
public class MyFrameworkSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFrameworkSpringbootApplication.class, args);
    }

}
