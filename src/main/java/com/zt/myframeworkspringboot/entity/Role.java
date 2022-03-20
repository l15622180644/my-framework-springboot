package com.zt.myframeworkspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色表
 *
 * @author
 * @since 2022-03-20
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 角色名称 */
    private String roleName;

    /** 状态（0停用，1正常） */
    private Integer status;

    /** 创建时间 */
    private Long createTime;

    /** 修改时间 */
    private Long updateTime;

    @TableField(exist = false)
    private List<Long> menuIds;

    @TableField(exist = false)
    private List<Menu> menuList;


}
