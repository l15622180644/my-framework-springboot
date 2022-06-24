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

    BaseResult<Menu> getMenuOne(BaseParam param);

    BaseResult<Boolean> addMenu(Menu menu);

    BaseResult<Boolean> updateMenu(Menu menu);

    boolean delMenu(Long id);

    BaseResult<Boolean> bathDelMenu(BaseParam param);

    List<String> getPermsByUser(Long userId);

    List<Menu> getMenuTree(BaseParam param);

    List<Menu> getMenuTreeByUser(BaseParam param);

    List<Menu> getMenuTreeByRole(BaseParam param);

    BaseResult<List<Menu>> getChildMenu(BaseParam param);

}
