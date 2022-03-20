package com.zt.myframeworkspringboot.security.config;


import com.zt.myframeworkspringboot.security.core.filter.JWTAuthenticationTokenFilter;
import com.zt.myframeworkspringboot.security.core.handle.AccessDeniedHandlerImpl;
import com.zt.myframeworkspringboot.security.core.handle.AuthenticationEntryPointImpl;
import com.zt.myframeworkspringboot.security.core.provider.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;


@Configuration
//开启方法级别校验
//（prePostEnabled：解锁 @PreAuthorize 和 @PostAuthorize 两个注解，@PreAuthorize 注解会在方法执行前进行验证，而 @PostAuthorize 注解会在方法执行后进行验证）
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Resource
    @Lazy
    private LogoutSuccessHandler logoutSuccessHandler;

    @Resource
    @Lazy
    private JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    @Lazy
    private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    //核心配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            //允许跨域
            .cors().configurationSource(corsConfigurationSource()).and()
            //关闭csrf，否则会对post等请求进行防护
            .csrf().disable()
            //基于token机制（jwt托管信息），调整为让Spring Security不创建和使用session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            //禁用X-Frame-Options
            .headers().frameOptions().disable().and()
            //未登录处理
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
            //权限不足处理
            .accessDeniedHandler(accessDeniedHandler).and()
            //登出处理
            .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);

        //请求过滤
        http.authorizeHttpRequests()
                .antMatchers("/usersService/login").permitAll()
                .antMatchers("/sysService/captcha").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    //cors配置
    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 同源配置，*表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
        corsConfiguration.addAllowedHeader("*");// header，允许哪些header
        corsConfiguration.addAllowedMethod("*"); // 允许的请求方法，PSOT、GET等
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**", corsConfiguration); // 配置允许跨域访问的url
        return source;
    }

    //注入，自定义登录接口用到
    @Bean
    @Override
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(usernamePasswordAuthenticationProvider);
        super.configure(auth);
    }
}
