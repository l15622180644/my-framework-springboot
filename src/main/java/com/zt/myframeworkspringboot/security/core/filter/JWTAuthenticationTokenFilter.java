package com.zt.myframeworkspringboot.security.core.filter;



import com.zt.myframeworkspringboot.common.exception.GlobalExceptionHandler;
import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.common.util.ServletUtil;
import com.zt.myframeworkspringboot.security.config.SecurityProperties;
import com.zt.myframeworkspringboot.security.core.SecurityUserInfo;
import com.zt.myframeworkspringboot.security.core.service.SecurityService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 过滤器，验证 token 的有效性
 *
 */

@AllArgsConstructor
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {

//    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationTokenFilter.class);

    private final SecurityProperties properties;

    private final SecurityService securityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
//        LOGGER.info("请求URI ----------------------------------------- {}",uri);
        String token = request.getHeader(properties.getHead());
        if(StringUtils.isNotBlank(token)){
            SecurityUserInfo securityUserInfo = securityService.verifyTokenAndRefresh(token);
            if(securityUserInfo!=null){
                if(request.getMethod().equals("GET")&&uri.equals("/getUserInfo")){
                    ServletUtil.returnJSON(response,securityUserInfo);
                    return;
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUserInfo, null, securityUserInfo.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //设置到security上下文
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else {
                ServletUtil.returnJSON(response, Status.TOKENTIMEOUT,"身份验证失败");
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
}
