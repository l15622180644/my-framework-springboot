package com.zt.myframeworkspringboot.service.impl;

import com.zt.myframeworkspringboot.entity.LogBack;
import com.zt.myframeworkspringboot.mapper.LogBackMapper;
import com.zt.myframeworkspringboot.service.LogBackService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.common.status.Status;

/**
 *
 * @author
 * @since 2022-03-21
 */

@Service
public class LogBackServiceImpl extends ServiceImpl<LogBackMapper, LogBack> implements LogBackService {

    @Override
    public BaseResult getLogBackPage(BaseParam param){
        Page<LogBack> page = lambdaQuery()
                .orderBy(true,param.getIsASC()!=null?param.getIsASC():false,LogBack::getCreateTime)
                .page(param.getPage(entityClass));
        return BaseResult.returnResult(page);
    }

    @Override
    public BaseResult<LogBack> getLogBackOne(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(getById(param.getId()));
    }

    @Override
    public BaseResult<Boolean> addLogBack(LogBack logBack){
        return BaseResult.returnResult(save(logBack),logBack.getId());
    }

    @Override
    public BaseResult<Boolean> updateLogBack(LogBack logBack){
        if (logBack.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(updateById(logBack));
    }

    @Override
    public BaseResult<Boolean> delLogBack(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(removeById(param.getId()));
    }

    @Override
    public BaseResult<Boolean> bathDelLogBack(BaseParam param){
        if (param.getIds() == null || param.getIds().isEmpty()) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(removeByIds(param.getIds()));
    }

}
