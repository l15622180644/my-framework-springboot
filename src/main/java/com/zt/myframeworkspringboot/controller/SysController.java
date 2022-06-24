package com.zt.myframeworkspringboot.controller;

import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.entity.LogBack;
import com.zt.myframeworkspringboot.service.LogBackService;
import com.zt.myframeworkspringboot.service.SysService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @module
 * @date 2022/3/21 10:07
 */

@RestController
@RequestMapping("/sysService")
public class SysController {

    @Resource
    private SysService sysService;

    @Resource
    private LogBackService logBackService;

    @GetMapping("/captcha")
    public BaseResult<Map<String,Object>> captcha(){
        return sysService.captcha();
    }

    @PostMapping("/getLogBackPage")
    public BaseResult<List<LogBack>> getLogBackPage(@RequestBody BaseParam param){
        return logBackService.getLogBackPage(param);
    }

}
