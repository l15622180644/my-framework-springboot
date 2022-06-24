package com.zt.myframeworkspringboot.service.impl;

import com.zt.myframeworkspringboot.entity.RoleMenu;
import com.zt.myframeworkspringboot.mapper.RoleMenuMapper;
import com.zt.myframeworkspringboot.service.RoleMenuService;
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
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public BaseResult getRoleMenuPage(BaseParam baseParam){
        Page<RoleMenu> page = lambdaQuery().page(baseParam.getPage(entityClass));
        return BaseResult.returnResult(page);
    }

    @Override
    public BaseResult<RoleMenu> getRoleMenuOne(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(getById(param.getId()));
    }

    @Override
    public BaseResult<Boolean> addRoleMenu(RoleMenu roleMenu){
        return BaseResult.returnResult(save(roleMenu),roleMenu.getId());
    }

    @Override
    public BaseResult<Boolean> updateRoleMenu(RoleMenu roleMenu){
        if (roleMenu.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(updateById(roleMenu));
    }

    @Override
    public BaseResult<Boolean> delRoleMenu(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(removeById(param.getId()));
    }

    @Override
    public BaseResult<Boolean> bathDelRoleMenu(BaseParam param){
        if (param.getIds() == null || param.getIds().isEmpty()) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(removeByIds(param.getIds()));
    }

}
