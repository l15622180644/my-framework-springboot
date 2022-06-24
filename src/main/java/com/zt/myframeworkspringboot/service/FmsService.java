package com.zt.myframeworkspringboot.service;



import com.zt.myframeworkspringboot.common.base.BaseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FmsService {

    BaseResult upload(HttpServletRequest request);

    void downLoad(String filePath, HttpServletResponse response, boolean isOnLine, String fName, String kk) throws Exception;

    BaseResult getKey(Integer num, HttpServletRequest request);

    void removeTimeOutKey();

    BaseResult uploadVideo(HttpServletRequest request);

    void showPic(String filePath, HttpServletResponse response) throws Exception;

    void showVideo(String filePath, HttpServletResponse response) throws IOException;

}
