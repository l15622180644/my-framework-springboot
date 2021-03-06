package com.zt.myframeworkspringboot.controller;


import com.zt.myframeworkspringboot.common.annotation.Log;
import com.zt.myframeworkspringboot.service.RoleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import com.zt.myframeworkspringboot.entity.Role;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

import javax.annotation.Resource;

/**
 * 系统角色
 *
 * @author
 * @since 2022-03-20
 */

@RestController
@RequestMapping("/roleService")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 查询列表
     */
    @PostMapping("/getRolePage")
    public BaseResult getPage(@RequestBody BaseParam param){
        return roleService.getRolePage(param);
    }

    /**
     * 获取详情
     */
    @PostMapping("/getRoleOne")
    public BaseResult getOne(@RequestBody BaseParam param){
        return roleService.getRoleOne(param);
    }

    /**
     * 新增
     */
    @PostMapping("/addRole")
    @Transactional
    @Log(module = "系统角色",content = "新增",type = Log.INSERT)
    public BaseResult add(@RequestBody Role role){
        return roleService.addRole(role);
    }

    /**
     * 修改
     */
    @PostMapping("/updateRole")
    @Transactional
    @Log(module = "系统角色",content = "修改",type = Log.UPDATE)
    public BaseResult update(@RequestBody Role role){
        return roleService.updateRole(role);
    }

    /**
     * 删除
     */
    @PostMapping("/delRole")
    @Transactional
    @Log(module = "系统角色",content = "删除",type = Log.DELETE)
    public BaseResult del(@RequestBody BaseParam param){
        return BaseResult.returnResult(roleService.delRole(param.getId()));
    }

    /**
     * 批量删除
     */
    @PostMapping("/bathDelRole")
    @Transactional
    @Log(module = "系统角色",content = "批量删除",type = Log.DELETE)
    public BaseResult bathDel(@RequestBody BaseParam param){
        return roleService.bathDelRole(param);
    }

    /**
     * 修改角色权限
     * @param role
     * @return
     */
    @PostMapping("/updateRolePrivilege")
    @Transactional
    @Log(module = "系统角色",content = "修改权限",type = Log.UPDATE)
    public BaseResult updateRolePrivilege(@RequestBody Role role){
        return roleService.updateRolePrivilege(role);
    }


}
