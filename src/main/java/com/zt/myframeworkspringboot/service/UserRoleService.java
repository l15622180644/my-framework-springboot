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

    BaseResult<UserRole> getUserRoleOne(BaseParam param);

    BaseResult<Boolean> addUserRole(UserRole userRole);

    BaseResult<Boolean> updateUserRole(UserRole userRole);

    BaseResult<Boolean> delUserRole(BaseParam param);

    BaseResult<Boolean> bathDelUserRole(BaseParam param);

}
