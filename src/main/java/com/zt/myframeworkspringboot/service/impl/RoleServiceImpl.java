package com.zt.myframeworkspringboot.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zt.myframeworkspringboot.common.exception.CustomException;
import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.entity.Role;
import com.zt.myframeworkspringboot.entity.RoleMenu;
import com.zt.myframeworkspringboot.entity.UserRole;
import com.zt.myframeworkspringboot.entity.Users;
import com.zt.myframeworkspringboot.mapper.RoleMapper;
import com.zt.myframeworkspringboot.mapper.RoleMenuMapper;
import com.zt.myframeworkspringboot.mapper.UserRoleMapper;
import com.zt.myframeworkspringboot.mapper.UsersMapper;
import com.zt.myframeworkspringboot.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author
 * @since 2022-03-20
 */

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UsersMapper usersMapper;

    @Override
    public BaseResult getRolePage(BaseParam baseParam){
        Page<Role> page = lambdaQuery().page(baseParam.getPage(entityClass));
        return BaseResult.returnResult(page);
    }

    @Override
    public BaseResult getRoleOne(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        Role role = getById(param.getId());
        if(role!=null){

        }
        return BaseResult.returnResult(role);
    }

    @Override
    public BaseResult addRole(Role role){
        save(role);
        List<Long> menuIds = role.getMenuIds();
        if(menuIds!=null&&role.getId()!=null){
            menuIds.forEach(v->{
                RoleMenu roleMenu = new RoleMenu(role.getId(),v);
                if(roleMenuMapper.insert(roleMenu)!=1) throw new CustomException(Status.OPFAIL);
            });
        }
        return BaseResult.returnResult(role.getId()!=null,role.getId());
    }

    @Override
    public BaseResult updateRole(Role role){
        if (role.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        updateRolePrivilege(role);
        return BaseResult.returnResult(updateById(role));
    }

    @Override
    public boolean delRole(Long id){
        if (id == null) throw  new CustomException(Status.PARAMEXCEPTION);
        Role role = getById(id);
        if(haveUserByRole(id)) throw new CustomException("删除失败，原因："+role.getRoleName()+"存在绑定用户");
        roleMenuMapper.delete(Wrappers.lambdaQuery(RoleMenu.class).eq(RoleMenu::getRoleId,id));
        return removeById(id);
    }

    //是否存在用户角色绑定
    private boolean haveUserByRole(Long roleId){
        List<UserRole> userRoles = userRoleMapper.selectList(Wrappers.lambdaQuery(UserRole.class).eq(UserRole::getRoleId, roleId));
        List<Long> userIds = userRoles.stream().map(UserRole::getUserId).collect(Collectors.toList());
        if(userIds.isEmpty()){
            return false;
        }
        return usersMapper.selectCount(Wrappers.lambdaQuery(Users.class).in(Users::getId,userIds).eq(Users::getDeleted,0))>0;
    }

    @Override
    public BaseResult bathDelRole(BaseParam param){
        if (param.getIds() == null || param.getIds().isEmpty()) return BaseResult.error(Status.PARAMEXCEPTION);
        List<Long> ids = param.getIds();
        ids.forEach(v->{
            if(!delRole(v)) throw new CustomException(Status.OPFAIL);
        });
        return BaseResult.success(Status.OPSUCCESS);
    }

    @Override
    public List<Role> getRoleByUser(Long userId) {
        return roleMapper.getRoleByUser(Wrappers.query()
                .eq("a.user_id", userId)
                .eq("b.status", 1));
    }

    @Override
    public BaseResult updateRolePrivilege(Role role) {
        if (role.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        List<Long> menuIds = role.getMenuIds();
        if (menuIds!=null) {
            roleMenuMapper.delete(Wrappers.lambdaQuery(RoleMenu.class).eq(RoleMenu::getRoleId,role.getId()));
            menuIds.forEach(v->{
                if(roleMenuMapper.insert(new RoleMenu(role.getId(),v))!=1) throw new CustomException(Status.OPFAIL);
            });
        }
        return BaseResult.returnResult(true);
    }

}
