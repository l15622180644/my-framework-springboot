package com.zt.myframeworkspringboot.security.util;

import com.zt.myframeworkspringboot.security.core.SecurityUserInfo;
import org.springframework.security.core.context.SecurityContextHolder;


public class MySecurityUtil {

    public static SecurityUserInfo getUser(){
        return (SecurityUserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
