package com.zt.myframeworkspringboot.service;

import com.zt.myframeworkspringboot.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

import java.util.List;

/**
 *
 * @author
 * @since 2022-03-20
 */

public interface RoleService extends IService<Role> {

    BaseResult getRolePage(BaseParam param);

    BaseResult<Role> getRoleOne(BaseParam param);

    BaseResult<Boolean> addRole(Role role);

    BaseResult<Boolean> updateRole(Role role);

    boolean delRole(Long id);

    BaseResult<Boolean> bathDelRole(BaseParam param);

    List<Role> getRoleByUser(Long userId);

    BaseResult<Boolean> updateRolePrivilege(Role role);

}
