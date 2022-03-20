package com.zt.myframeworkspringboot.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zt.myframeworkspringboot.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author
 * @since 2022-03-20
 */

public interface MenuMapper extends BaseMapper<Menu> {

    @Select("select DISTINCT d.permission" +
            " FROM user_role a LEFT JOIN role b on a.role_id = b.id LEFT JOIN role_menu c ON b.id = c.role_id LEFT JOIN menu d ON c.menu_id = d.id ${ew.CustomSqlSegment}")
    List<String> getPermsByUser(@Param(Constants.WRAPPER) Wrapper ew);

    @Select("SELECT DISTINCT d.* FROM user_role a LEFT JOIN role b on a.role_id = b.id LEFT JOIN role_menu c ON b.id = c.role_id LEFT JOIN menu d ON c.menu_id = d.id ${ew.CustomSqlSegment}")
    List<Menu> getMenuByUser(@Param(Constants.WRAPPER) Wrapper ew);

    @Select("SELECT DISTINCT b.* FROM role_menu a LEFT JOIN menu b ON a.menu_id = b.id ${ew.CustomSqlSegment}")
    List<Menu> getMenuByRole(@Param(Constants.WRAPPER) Wrapper ew);

}
