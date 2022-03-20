package com.zt.myframeworkspringboot.security.config;

import com.zt.myframeworkspringboot.security.core.filter.JWTAuthenticationTokenFilter;
import com.zt.myframeworkspringboot.security.core.handle.AccessDeniedHandlerImpl;
import com.zt.myframeworkspringboot.security.core.handle.AuthenticationEntryPointImpl;
import com.zt.myframeworkspringboot.security.core.handle.LogoutSuccessHandlerImpl;
import com.zt.myframeworkspringboot.security.core.provider.UsernamePasswordAuthenticationProvider;
import com.zt.myframeworkspringboot.security.core.service.SecurityService;
import com.zt.myframeworkspringboot.security.core.service.impl.DefaultSecurityServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;

/**
 * Spring Security 自动配置类，主要用于相关组件的配置
 */

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfig {

    @Resource
    private SecurityProperties securityProperties;

    /**
     * 当还没有实现SecurityService接口时就给他一个默认的实现
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SecurityService.class)
    public SecurityService defaultSecurityServiceImpl(){
        return new DefaultSecurityServiceImpl();
    }

    @Bean
    public JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter(SecurityService securityService){
        return new JWTAuthenticationTokenFilter(securityProperties,securityService);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationEntryPointImpl();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(SecurityService securityService){
        return new LogoutSuccessHandlerImpl(securityProperties,securityService);
    }

    @Bean
    public UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider(SecurityService securityService){
        return new UsernamePasswordAuthenticationProvider(securityService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
