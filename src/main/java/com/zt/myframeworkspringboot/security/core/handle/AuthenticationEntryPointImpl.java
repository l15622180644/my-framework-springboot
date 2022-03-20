package com.zt.myframeworkspringboot.security.core.handle;



import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.common.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问一个需要认证的 URL 资源，但是此时自己尚未认证（登录）的情况下，返回错误码，从而使前端重定向到登录页
 * Spring Security 通过 ExceptionTranslationFilter 方法，调用当前类
 *
 */
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ServletUtil.returnJSON(response, Status.TOKENNOTEXIST);
    }
}
