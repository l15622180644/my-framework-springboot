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
    public BaseResult getMenuPage(BaseParam baseParam){
        Page<Menu> page = lambdaQuery().page(baseParam.getPage(entityClass));
        return BaseResult.returnResult(page);
    }

    @Override
    public BaseResult getMenuOne(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(getById(param.getId()));
    }

    @Override
    public BaseResult addMenu(Menu menu){
        return BaseResult.returnResult(save(menu),menu.getId());
    }

    @Override
    public BaseResult updateMenu(Menu menu){
        if (menu.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(updateById(menu));
    }

    @Override
    public boolean delMenu(Long id){
        if (id == null) throw  new CustomException(Status.PARAMEXCEPTION);
        Menu menu = getById(id);
        if(checkMenuExistRole(id)) throw new CustomException("删除失败，原因："+menu.getMenuName()+"存在角色绑定");
        if(haveChild(id)) throw new CustomException("删除失败，原因："+menu.getMenuName()+"存在子节点");
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
    public BaseResult bathDelMenu(BaseParam param){
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
    public BaseResult getMenuTree(BaseParam param) {
        List<Menu> list = list(Wrappers.lambdaQuery(entityClass));
        return BaseResult.returnResult(TreeUtils.listToTree(Menu.class, JSONArray.toJSONString(list),"parentId","id","children","sort",-1));
    }

    @Override
    public List<Menu> getMenuTreeByUser(Long id,Integer type) {
        if(id==null) throw new CustomException(Status.PARAMEXCEPTION);
        List<Menu> menu = menuMapper.getMenuByUser(Wrappers.query()
                .eq("a.user_id", id)
                .eq("b.status", 1)
                .eq("d.status", 1)
                .eq(type!=null,"d.menu_type",type)
        );
        return TreeUtils.listToTree(Menu.class, JSONArray.toJSONString(menu),"parentId","id","children","sort",-1);
    }

    @Override
    public List<Menu> getMenuTreeByRole(Long id,Integer type) {
        if(id==null) throw new CustomException(Status.PARAMEXCEPTION);
        List<Menu> menu = menuMapper.getMenuByRole(Wrappers.query()
                .eq("a.role_id", id)
                .eq(type!=null,"b.menu_type",type)
        );
        return TreeUtils.listToTree(Menu.class, JSONArray.toJSONString(menu),"parentId","id","children","sort",-1);
    }

    @Override
    public BaseResult getChildMenu(BaseParam param) {
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
