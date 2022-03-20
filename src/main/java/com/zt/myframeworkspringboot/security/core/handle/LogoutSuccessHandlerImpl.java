package com.zt.myframeworkspringboot.security.core.handle;


import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.common.util.ServletUtil;
import com.zt.myframeworkspringboot.security.config.SecurityProperties;
import com.zt.myframeworkspringboot.security.core.service.SecurityService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出处理器
 */

@AllArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final SecurityProperties properties;

    private final SecurityService securityService;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader(properties.getHead());
        if(StringUtils.isNotBlank(token)){
            securityService.logout(token);
        }
//        ServletUtil.returnJSON(response, Status.OPFAIL,null);
    }
}
