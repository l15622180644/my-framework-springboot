package com.zt.myframeworkspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志表
 *
 * @author
 * @since 2022-03-21
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class LogBack implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 操作人id */
    private Long userId;

    /** 操作人名称 */
    private String userName;

    /** 模块 */
    private String module;

    /** 内容 */
    private String content;

    /** 操作（-1操作、0插入、1更新、2删除） */
    private Integer opp;

    /** 状态（0成功1失败） */
    private Integer status;

    /** ip地址 */
    private String addr;

    /** 操作平台（0pc端 、 1移动端） */
    private Integer plat;

    /** 操作时间 */
    private Long createTime;


}
