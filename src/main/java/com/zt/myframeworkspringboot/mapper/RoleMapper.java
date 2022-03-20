package com.zt.myframeworkspringboot.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zt.myframeworkspringboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author
 * @since 2022-03-20
 */

public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT b.* FROM user_role a LEFT JOIN role b ON a.role_id = b.id ${ew.CustomSqlSegment}")
    public List<Role> getRoleByUser(@Param(Constants.WRAPPER) Wrapper ew);

}
