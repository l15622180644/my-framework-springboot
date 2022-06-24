package com.zt.myframeworkspringboot.service;

import com.zt.myframeworkspringboot.entity.LogBack;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;

/**
 *
 * @author
 * @since 2022-03-21
 */

public interface LogBackService extends IService<LogBack> {

    BaseResult getLogBackPage(BaseParam param);

    BaseResult<LogBack> getLogBackOne(BaseParam param);

    BaseResult<Boolean> addLogBack(LogBack logBack);

    BaseResult<Boolean> updateLogBack(LogBack logBack);

    BaseResult<Boolean> delLogBack(BaseParam param);

    BaseResult<Boolean> bathDelLogBack(BaseParam param);

}
