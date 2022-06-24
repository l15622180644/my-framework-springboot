package com.zt.myframeworkspringboot.security.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zt.myframeworkspringboot.common.exception.CustomException;
import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.entity.Users;
import com.zt.myframeworkspringboot.security.core.SecurityUserInfo;
import com.zt.myframeworkspringboot.service.MenuService;
import com.zt.myframeworkspringboot.service.RoleService;
import com.zt.myframeworkspringboot.service.UsersService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author
 * @module
 * @date 2022/6/10 8:48
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UsersService usersService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Users users = usersService.getOne(Wrappers.lambdaQuery(Users.class).eq(Users::getLoginName,username));
        if(users==null) throw new CustomException(Status.LOGINFAILCAUSEPWD);
        users.setRoleList(roleService.getRoleByUser(users.getId()));
        return new SecurityUserInfo(users,menuService.getPermsByUser(users.getId()));
    }

    public UserDetails xcxLoadUserByPhone(String phone) {
        /*Users users = usersService.getOne(Wrappers.lambdaQuery(Users.class).eq(Users::getPhone, phone));
        if(users==null) throw new CustomException(Status.LOGINFAILCAUSEPWD);
        users.setRoleList(frontRoleService.getRoleByUser(users.getId()));
        users.setClassList(gradeClassService.getGradeClassByUser(users.getId()));
        return new SecurityUserInfo(users,frontMenuService.getPermsByUser(users.getId()));*/
        return null;
    }
}
