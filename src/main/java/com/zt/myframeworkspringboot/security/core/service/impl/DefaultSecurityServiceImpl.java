package com.zt.myframeworkspringboot.security.core.service.impl;

import com.zt.myframeworkspringboot.security.core.SecurityUserInfo;
import com.zt.myframeworkspringboot.security.core.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultSecurityServiceImpl implements SecurityService {

    @Override
    public SecurityUserInfo verifyTokenAndRefresh(String token) {
        return null;
    }

    @Override
    public void logout(String token) {

    }

}
