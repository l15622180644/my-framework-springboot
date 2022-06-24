package com.zt.myframeworkspringboot.config.web;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.*;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 拦截器配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    /**
     * 路径映射（页面跳转）
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    /**
     * 自定义静态资源映射目录
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }


}
