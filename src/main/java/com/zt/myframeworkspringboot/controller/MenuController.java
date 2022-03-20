package com.zt.myframeworkspringboot.controller;


import com.zt.myframeworkspringboot.service.MenuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import com.zt.myframeworkspringboot.entity.Menu;
import javax.annotation.Resource;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

/**
 * 菜单
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
    public BaseResult getPage(@RequestBody BaseParam param){
        return menuService.getMenuPage(param);
    }

    /**
     * 获取详情
     */
    @PostMapping("/getMenuOne")
    public BaseResult getOne(@RequestBody BaseParam param){
        return menuService.getMenuOne(param);
    }

    /**
     * 新增
     */
    @PostMapping("/addMenu")
    @Transactional
    public BaseResult add(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    /**
     * 修改
     */
    @PostMapping("/updateMenu")
    @Transactional
    public BaseResult update(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    /**
     * 删除
     */
    @PostMapping("/delMenu")
    @Transactional
    public BaseResult del(@RequestBody BaseParam param){
        return BaseResult.returnResult(menuService.delMenu(param.getId()));
    }

    /**
     * 批量删除
     */
    @PostMapping("/bathDelMenu")
    @Transactional
    public BaseResult bathDel(@RequestBody BaseParam param){
        return menuService.bathDelMenu(param);
    }

    /**
     * 获取子级菜单
     * @param param
     * @return
     */
    @PostMapping("/getChildMenu")
    public BaseResult getChildMenu(@RequestBody BaseParam param){
        return menuService.getChildMenu(param);
    }

    /**
     * 获取系统全部菜单
     * @param param
     * @return
     */
    @PostMapping("/getMenuTree")
    public BaseResult getMenuTree(@RequestBody BaseParam param){
        return menuService.getMenuTree(param);
    }

    /**
     * 获取用户的菜单
     * @param param
     * @return
     */
    @PostMapping("/getMenuTreeByUser")
    public BaseResult getMenuTreeByUser(@RequestBody BaseParam param){
        return BaseResult.returnResult(menuService.getMenuTreeByUser(param.getId(),param.getType()));
    }

    /**
     * 获取角色菜单
     * @param param
     * @return
     */
    @PostMapping("/getMenuTreeByRole")
    public BaseResult getMenuTreeByRole(@RequestBody BaseParam param){
        return BaseResult.returnResult(menuService.getMenuTreeByRole(param.getId(),param.getType()));
    }


}
