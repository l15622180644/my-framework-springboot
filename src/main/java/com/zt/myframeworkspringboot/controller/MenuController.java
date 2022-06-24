package com.zt.myframeworkspringboot.controller;


import com.zt.myframeworkspringboot.common.annotation.Log;
import com.zt.myframeworkspringboot.service.MenuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import com.zt.myframeworkspringboot.entity.Menu;
import javax.annotation.Resource;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

import java.util.List;

/**
 * 菜单权限
 *
 * @author
 * @since 2022-03-20
 */

@RestController
@RequestMapping("/menuService")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 查询列表
     */
    @PostMapping("/getMenuPage")
    public BaseResult<List<Menu>> getPage(@RequestBody BaseParam param){
        return menuService.getMenuPage(param);
    }

    /**
     * 获取详情
     */
    @PostMapping("/getMenuOne")
    public BaseResult<Menu> getOne(@RequestBody BaseParam param){
        return menuService.getMenuOne(param);
    }

    /**
     * 新增
     */
    @PostMapping("/addMenu")
    @Transactional
    @Log(module = "菜单权限",content = "新增",type = Log.INSERT)
    public BaseResult<Boolean> add(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    /**
     * 修改
     */
    @PostMapping("/updateMenu")
    @Transactional
    @Log(module = "菜单权限",content = "修改",type = Log.UPDATE)
    public BaseResult<Boolean> update(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    /**
     * 删除
     */
    @PostMapping("/delMenu")
    @Transactional
    @Log(module = "菜单权限",content = "删除",type = Log.DELETE)
    public BaseResult<Boolean> del(@RequestBody BaseParam param){
        return BaseResult.returnResult(menuService.delMenu(param.getId()));
    }

    /**
     * 批量删除
     */
    @PostMapping("/bathDelMenu")
    @Transactional
    @Log(module = "菜单权限",content = "批量删除",type = Log.DELETE)
    public BaseResult<Boolean> bathDel(@RequestBody BaseParam param){
        return menuService.bathDelMenu(param);
    }

    /**
     * 获取子级菜单
     * @param param
     * @return
     */
    @PostMapping("/getChildMenu")
    public BaseResult<List<Menu>> getChildMenu(@RequestBody BaseParam param){
        return menuService.getChildMenu(param);
    }

    /**
     * 获取系统全部菜单
     * @param param
     * @return
     */
    @PostMapping("/getMenuTree")
    public BaseResult<List<Menu>> getMenuTree(@RequestBody BaseParam param){
        return BaseResult.returnResult(menuService.getMenuTree(param));
    }

    /**
     * 获取用户的菜单
     * @param param
     * @return
     */
    @PostMapping("/getMenuTreeByUser")
    public BaseResult<List<Menu>> getMenuTreeByUser(@RequestBody BaseParam param){
        return BaseResult.returnResult(menuService.getMenuTreeByUser(param));
    }

    /**
     * 获取角色菜单
     * @param param
     * @return
     */
    @PostMapping("/getMenuTreeByRole")
    public BaseResult<List<Menu>> getMenuTreeByRole(@RequestBody BaseParam param){
        return BaseResult.returnResult(menuService.getMenuTreeByRole(param));
    }


}
