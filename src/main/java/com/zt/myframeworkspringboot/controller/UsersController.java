package com.zt.myframeworkspringboot.controller;


import com.zt.myframeworkspringboot.param.LoginParam;
import com.zt.myframeworkspringboot.param.ResetPWParam;
import com.zt.myframeworkspringboot.service.UsersService;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import com.zt.myframeworkspringboot.entity.Users;
import javax.annotation.Resource;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

/**
 * 用户
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
    public BaseResult getPage(@RequestBody BaseParam param){
        return usersService.getUsersPage(param);
    }

    /**
     * 获取详情
     */
    @PostMapping("/getUsersOne")
    public BaseResult getOne(@RequestBody BaseParam param){
        return usersService.getUsersOne(param);
    }

    /**
     * 新增
     */
    @PostMapping("/addUsers")
    @Transactional
    public BaseResult add(@RequestBody Users users){
        return usersService.addUsers(users);
    }

    /**
     * 修改
     */
    @PostMapping("/updateUsers")
    @Transactional
    public BaseResult update(@RequestBody Users users){
        return usersService.updateUsers(users);
    }

    /**
     * 删除
     */
    @PostMapping("/delUsers")
    @Transactional
    public BaseResult del(@RequestBody BaseParam param){
        return usersService.delUsers(param);
    }

    /**
     * 批量删除
     */
    @PostMapping("/bathDelUsers")
    @Transactional
    public BaseResult bathDel(@RequestBody BaseParam param){
        return usersService.bathDelUsers(param);
    }

    /**
     * 登录
     * @param param
     * @return
     */
    @PostMapping("/login")
    public BaseResult login(@RequestBody LoginParam param){
        return usersService.login(param);
    }

    /**
     * 重置密码
     * @param param
     * @return
     */
    @PostMapping("/resetPassword")
    @Transactional
    public BaseResult resetPassword(@RequestBody ResetPWParam param){
        return usersService.resetPassword(param);
    }


}
