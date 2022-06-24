package com.zt.myframeworkspringboot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zt.myframeworkspringboot.common.exception.CustomException;
import com.zt.myframeworkspringboot.common.util.TreeUtils;
import com.zt.myframeworkspringboot.entity.Menu;
import com.zt.myframeworkspringboot.entity.RoleMenu;
import com.zt.myframeworkspringboot.mapper.MenuMapper;
import com.zt.myframeworkspringboot.mapper.RoleMenuMapper;
import com.zt.myframeworkspringboot.service.MenuService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.common.status.Status;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author
 * @since 2022-03-20
 */

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public BaseResult getMenuPage(BaseParam param){
        Page<Menu> page = lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()),Menu::getMenuName,param.getName())
                .eq(param.getType()!=null,Menu::getMenuType,param.getType())
                .eq(param.getStatus()!=null,Menu::getStatus,param.getStatus())
                .eq(param.getId()!=null,Menu::getParentId,param.getId())
                .orderBy(true,param.getIsASC()!=null?param.getIsASC():false,Menu::getCreateTime)
                .page(param.getPage(entityClass));
        return BaseResult.returnResult(page);
    }

    @Override
    public BaseResult<Menu> getMenuOne(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(getById(param.getId()));
    }

    @Override
    public BaseResult<Boolean> addMenu(Menu menu){
        return BaseResult.returnResult(save(menu),menu.getId());
    }

    @Override
    public BaseResult<Boolean> updateMenu(Menu menu){
        if (menu.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(updateById(menu));
    }

    @Override
    public boolean delMenu(Long id){
        if (id == null) throw  new CustomException(Status.PARAMEXCEPTION);
        Menu menu = getById(id);
//        if(checkMenuExistRole(id)) throw new CustomException("删除失败，原因：【"+menu.getMenuName()+"】存在绑定角色");
        if(haveChild(id)) throw new CustomException("删除失败，原因："+menu.getMenuName()+"存在子节点");
        roleMenuMapper.delete(Wrappers.lambdaQuery(RoleMenu.class).eq(RoleMenu::getMenuId,id));
        return removeById(id);
    }

    //是否存在子节点
    private boolean haveChild(Long id){
        return count(Wrappers.lambdaQuery(Menu.class).eq(Menu::getParentId,id)) > 0;
    }

    //是否存在角色绑定
    private boolean checkMenuExistRole(Long id){
        return roleMenuMapper.selectCount(Wrappers.lambdaQuery(RoleMenu.class).eq(RoleMenu::getMenuId,id)) > 0;
    }

    @Override
    public BaseResult<Boolean> bathDelMenu(BaseParam param){
        if (param.getIds() == null || param.getIds().isEmpty()) return BaseResult.error(Status.PARAMEXCEPTION);
        List<Long> ids = param.getIds();
        ids.forEach(v->{
            if(!delMenu(v)) throw new CustomException(Status.OPFAIL);
        });
        return BaseResult.returnResult(removeByIds(param.getIds()));
    }

    @Override
    public List<String> getPermsByUser(Long userId) {
        List<String> perms = menuMapper.getPermsByUser(Wrappers.query()
                .eq("a.user_id", userId)
                .eq("b.status", 1)
                .eq("d.status", 1));
        return perms;
    }

    @Override
    public List<Menu> getMenuTree(BaseParam param) {
        List<Menu> list = list(Wrappers.lambdaQuery(entityClass)
            .eq(param.getType()!=null,Menu::getMenuType,param.getType())
            .eq(param.getStatus()!=null,Menu::getStatus,param.getStatus()));
        return TreeUtils.listToTree(Menu.class, JSONArray.toJSONString(list),"parentId","id","children","sort",-1);
    }

    @Override
    public List<Menu> getMenuTreeByUser(BaseParam param) {
        if(param.getId()==null) throw new CustomException(Status.PARAMEXCEPTION);
        List<Menu> menu = menuMapper.getMenuByUser(Wrappers.query()
                .eq("a.user_id", param.getId())
                .eq("b.status", 1)
                .eq("d.status", 1)
                .in(param.getTypes()!=null&&!param.getTypes().isEmpty(),"d.menu_type",param.getTypes())
        );
        return TreeUtils.listToTree(Menu.class, JSONArray.toJSONString(menu),"parentId","id","children","sort",-1);
    }

    @Override
    public List<Menu> getMenuTreeByRole(BaseParam param) {
        if(param.getId()==null) throw new CustomException(Status.PARAMEXCEPTION);
        //查询角色拥有的权限id
        List<Long> menuIds = roleMenuMapper.selectList(Wrappers.lambdaQuery(RoleMenu.class).select(RoleMenu::getMenuId).eq(RoleMenu::getRoleId, param.getId())).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        List<Menu> menuTree = getMenuTree(new BaseParam());
        if (!menuIds.isEmpty()) {
            menuTree.forEach(v->{
                if(menuIds.contains(v.getId())){
                    v.setRoleFlag(true);
                }else {
                    v.setRoleFlag(false);
                }
                setRoleFlagForChild(v.getChildren(),menuIds);
            });
        }
        return menuTree;
    }

    void setRoleFlagForChild(List<Menu> list,List menuIds){
        list.forEach(v->{
            if(menuIds.contains(v.getId())){
                v.setRoleFlag(true);
            }else {
                v.setRoleFlag(false);
            }
            setRoleFlagForChild(v.getChildren(),menuIds);
        });
    }

    @Override
    public BaseResult<List<Menu>> getChildMenu(BaseParam param) {
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(getChildMenu(param.getId(),param.getStatus(),param.getType()));
    }

    private List<Menu> getChildMenu(Long id,Integer status,Integer type) {
        List<Menu> list = list(Wrappers.lambdaQuery(Menu.class)
                .eq(Menu::getParentId, id)
                .eq(status!=null,Menu::getStatus, status)
                .eq(type!=null,Menu::getMenuType,type)
                .orderByAsc(Menu::getSort));
        for(Menu menu : list){
            List<Menu> childMenu = getChildMenu(menu.getId(),status,type);
            menu.setChildren(childMenu);
        }
        return list;
    }

}
