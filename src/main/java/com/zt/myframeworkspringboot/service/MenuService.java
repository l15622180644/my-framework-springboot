package com.zt.myframeworkspringboot.service;

import com.alibaba.fastjson.JSONObject;
import com.zt.myframeworkspringboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

import java.util.List;

/**
 *
 * @author
 * @since 2022-03-20
 */

public interface MenuService extends IService<Menu> {

    BaseResult getMenuPage(BaseParam param);

    BaseResult getMenuOne(BaseParam param);

    BaseResult addMenu(Menu menu);

    BaseResult updateMenu(Menu menu);

    boolean delMenu(Long id);

    BaseResult bathDelMenu(BaseParam param);

    List<String> getPermsByUser(Long userId);

    BaseResult getMenuTree(BaseParam param);

    List<Menu> getMenuTreeByUser(Long id,Integer type);

    List<Menu> getMenuTreeByRole(Long id,Integer type);

    BaseResult getChildMenu(BaseParam param);

}
