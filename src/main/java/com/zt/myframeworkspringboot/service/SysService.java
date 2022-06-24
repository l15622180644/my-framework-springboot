package com.zt.myframeworkspringboot.service;

import com.zt.myframeworkspringboot.common.base.BaseResult;

import java.util.Map;

/**
 * @author
 * @module
 * @date 2022/3/21 10:09
 */
public interface SysService {

    BaseResult<Map<String,Object>> captcha();
}
