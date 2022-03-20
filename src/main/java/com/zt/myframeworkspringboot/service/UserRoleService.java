package com.zt.myframeworkspringboot.service;

import com.zt.myframeworkspringboot.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

/**
 *
 * @author
 * @since 2022-03-20
 */

public interface UserRoleService extends IService<UserRole> {

    BaseResult getUserRolePage(BaseParam param);

    BaseResult getUserRoleOne(BaseParam param);

    BaseResult addUserRole(UserRole userRole);

    BaseResult updateUserRole(UserRole userRole);

    BaseResult delUserRole(BaseParam param);

    BaseResult bathDelUserRole(BaseParam param);

}
