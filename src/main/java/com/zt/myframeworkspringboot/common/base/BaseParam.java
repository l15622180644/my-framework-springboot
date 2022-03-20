package com.zt.myframeworkspringboot.common.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class BaseParam implements Serializable {

    private Integer page;

    private Integer limit;

    private Long id;

    private List ids;

    private Integer type;

    private Integer status;

    public <T> Page<T> getPage(Class<T> t) {
        return new Page<T>(page != null ? page : 1, limit != null ? limit : 15);
    }

}
