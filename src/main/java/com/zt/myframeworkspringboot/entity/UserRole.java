package com.zt.myframeworkspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户-角色中间表
 *
 * @author
 * @since 2022-03-20
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 用户id */
    private Long userId;

    /** 角色id */
    private Long roleId;

    /** 创建时间 */
    private Long createTime;

    public UserRole (Long userId,Long roleId){
        this.userId = userId;
        this.roleId = roleId;
    }
}
