package com.zt.myframeworkspringboot.controller;


import com.zt.myframeworkspringboot.common.annotation.Log;
import com.zt.myframeworkspringboot.param.LoginParam;
import com.zt.myframeworkspringboot.param.ResetPWParam;
import com.zt.myframeworkspringboot.service.UsersService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import com.zt.myframeworkspringboot.entity.Users;
import javax.annotation.Resource;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

import java.util.List;

/**
 * 系统用户
 *
 * @author
 * @since 2022-03-20
 */

@RestController
@RequestMapping("/userService")
public class UsersController {

    @Resource
    private UsersService usersService;

    /**
     * 查询列表
     */
    @PostMapping("/getUsersPage")
    public BaseResult<List<Users>> getPage(@RequestBody BaseParam param){
        return usersService.getUsersPage(param);
    }

    /**
     * 获取详情
     */
    @PostMapping("/getUsersOne")
    public BaseResult<Users> getOne(@RequestBody BaseParam param){
        return usersService.getUsersOne(param);
    }

    /**
     * 新增
     */
    @PostMapping("/addUsers")
    @Transactional
    @Log(module = "系统用户",content = "新增",type = Log.INSERT)
    public BaseResult<Boolean> add(@RequestBody Users users){
        return usersService.addUsers(users);
    }

    /**
     * 修改
     */
    @PostMapping("/updateUsers")
    @Transactional
    @Log(module = "系统用户",content = "修改",type = Log.UPDATE)
    public BaseResult<Boolean> update(@RequestBody Users users){
        return usersService.updateUsers(users);
    }

    /**
     * 删除
     */
    @PostMapping("/delUsers")
    @Transactional
    @Log(module = "系统用户",content = "删除",type = Log.DELETE)
    public BaseResult<Boolean> del(@RequestBody BaseParam param){
        return usersService.delUsers(param);
    }

    /**
     * 批量删除
     */
    @PostMapping("/bathDelUsers")
    @Transactional
    @Log(module = "系统用户",content = "批量删除",type = Log.DELETE)
    public BaseResult<Boolean> bathDel(@RequestBody BaseParam param){
        return usersService.bathDelUsers(param);
    }

    /**
     * 登录
     * @param param
     * @return
     */
    @PostMapping("/login")
    @Log(module = "系统用户",content = "登录",type = Log.CUSTOM)
    public BaseResult<String> login(@RequestBody @Validated LoginParam param){
        return usersService.login(param);
    }

    /**
     * 重置密码
     * @param param
     * @return
     */
    @PostMapping("/resetPassword")
    @Transactional
    @Log(module = "系统用户",content = "重置密码",type = Log.UPDATE)
    public BaseResult<Boolean> resetPassword(@RequestBody ResetPWParam param){
        return usersService.resetPassword(param);
    }


}
