package com.zt.myframeworkspringboot.security.core.provider;


import com.zt.myframeworkspringboot.security.core.SecurityUserInfo;
import com.zt.myframeworkspringboot.security.core.service.SecurityService;
import com.zt.myframeworkspringboot.security.core.service.impl.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SecurityUserInfo userDetails = (SecurityUserInfo) userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
