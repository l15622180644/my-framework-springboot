package com.zt.myframeworkspringboot.service.impl;

import com.zt.myframeworkspringboot.entity.UserRole;
import com.zt.myframeworkspringboot.mapper.UserRoleMapper;
import com.zt.myframeworkspringboot.service.UserRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.common.status.Status;

/**
 *
 * @author
 * @since 2022-03-20
 */

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public BaseResult getUserRolePage(BaseParam baseParam){
        Page<UserRole> page = lambdaQuery().page(baseParam.getPage(entityClass));
        return BaseResult.returnResult(page);
    }

    @Override
    public BaseResult getUserRoleOne(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(getById(param.getId()));
    }

    @Override
    public BaseResult addUserRole(UserRole userRole){
        return BaseResult.returnResult(save(userRole),userRole.getId());
    }

    @Override
    public BaseResult updateUserRole(UserRole userRole){
        if (userRole.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(updateById(userRole));
    }

    @Override
    public BaseResult delUserRole(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(removeById(param.getId()));
    }

    @Override
    public BaseResult bathDelUserRole(BaseParam param){
        if (param.getIds() == null || param.getIds().isEmpty()) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(removeByIds(param.getIds()));
    }

}
