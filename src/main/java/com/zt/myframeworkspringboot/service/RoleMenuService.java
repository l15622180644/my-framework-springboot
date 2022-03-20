package com.zt.myframeworkspringboot.service;

import com.zt.myframeworkspringboot.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

/**
 *
 * @author
 * @since 2022-03-20
 */

public interface RoleMenuService extends IService<RoleMenu> {

    BaseResult getRoleMenuPage(BaseParam param);

    BaseResult getRoleMenuOne(BaseParam param);

    BaseResult addRoleMenu(RoleMenu roleMenu);

    BaseResult updateRoleMenu(RoleMenu roleMenu);

    BaseResult delRoleMenu(BaseParam param);

    BaseResult bathDelRoleMenu(BaseParam param);

}
