package com.zt.myframeworkspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单表
 *
 * @author
 * @since 2022-03-20
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 菜单名称 */
    private String menuName;

    /** 权限标识 */
    private String permission;

    /** 菜单类型（0目录 1菜单 2按钮） */
    private Integer menuType;

    /** 排序 */
    private Integer sort;

    /** 状态（0停用，1正常） */
    private Integer status;

    /** 父菜单ID */
    private Long parentId;

    /** 路由地址 */
    private String path;

    /** 菜单图标 */
    private String icon;

    /** 组件路径 */
    private String component;

    /** 创建时间 */
    private Long createTime;

    /** 修改时间 */
    private Long updateTime;

    @TableField(exist = false)
    private List<Menu> children;
}
