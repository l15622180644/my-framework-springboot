package com.zt.myframeworkspringboot.controller;



import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.service.FmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FmsController
 *
 * @author choison
 */
@RestController
@RequestMapping("/fmsService")
public class FmsController {

    @Autowired
    private FmsService fmsService;


    /**
     * 通用上传
     **/
    @PostMapping(value = "/upload")
    public BaseResult upload(HttpServletRequest request) {
        return fmsService.upload(request);
    }

    /**
     * 上传视频到指定路径
     **/
    @PostMapping(value = "/uploadVideo")
    public BaseResult uploadVedio(HttpServletRequest request) {
        return fmsService.uploadVideo(request);
    }

    /**
     * 通用下载
     **/
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine, String fName, String kk) throws Exception {
        fmsService.downLoad(filePath, response, isOnLine, fName, kk);
    }


    /**
     * 获取下载验证码
     **/
    @RequestMapping(value = "/getKey", method = RequestMethod.GET)
    public BaseResult getKey(Integer num, HttpServletRequest request) {
        return fmsService.getKey(num, request);
    }


    /**
     * 在线显示图片
     **/
    @RequestMapping(value = "/showPic", method = RequestMethod.GET)
    public void downLoad(String filePath, HttpServletResponse response) throws Exception {
        fmsService.showPic(filePath, response);
    }

    /**
     * 在线显示视屏
     **/
    @RequestMapping(value = "/showVideo", method = RequestMethod.GET)
    public void showVideo(String filePath, HttpServletResponse response) throws Exception {
        fmsService.showVideo(filePath, response);
    }
}
