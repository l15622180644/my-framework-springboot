package com.zt.myframeworkspringboot.service;

import com.zt.myframeworkspringboot.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.param.LoginParam;
import com.zt.myframeworkspringboot.param.ResetPWParam;
import com.zt.myframeworkspringboot.security.core.service.SecurityService;

/**
 *
 * @author
 * @since 2022-03-20
 */

public interface UsersService extends IService<Users>, SecurityService {

    BaseResult getUsersPage(BaseParam param);

    BaseResult<Users> getUsersOne(BaseParam param);

    BaseResult<Boolean> addUsers(Users users);

    BaseResult<Boolean> updateUsers(Users users);

    BaseResult<Boolean> delUsers(BaseParam param);

    BaseResult<Boolean> bathDelUsers(BaseParam param);

    BaseResult<String> login(LoginParam param);

    BaseResult<Boolean> resetPassword(ResetPWParam param);

    void reloadUserCache(Long userId);

}
