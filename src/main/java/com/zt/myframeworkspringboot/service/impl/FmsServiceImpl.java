package com.zt.myframeworkspringboot.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.common.util.AesUtil;
import com.zt.myframeworkspringboot.common.util.TimeHelper;
import com.zt.myframeworkspringboot.service.FmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;

@Service
public class FmsServiceImpl implements FmsService {

    @Value("${uploadPath}")
    private String UPLOAD_PATH;

    @Value("${videoPath}")
    private String VIDEO_PATH;

    private Map<String, String> map = new HashMap<>();

    private static List<String> typeList = new ArrayList<>();

    private static List<String> valueList = new ArrayList<>();

    static {
        typeList.add("image/png");
        typeList.add("image/jpeg");
        typeList.add("application/x-zip-compressed");
        typeList.add("application/msword");
        typeList.add("application/pdf");
        typeList.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        typeList.add("application/vnd.ms-excel");
        typeList.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        typeList.add("text/plain");
        typeList.add("application/octet-stream");
        typeList.add("application/vnd.openxmlformats-officedocument.presentationml.presentation");
        typeList.add("application/vnd.ms-powerpoint");
        valueList.add("png");
        valueList.add("jpg");
        valueList.add("jpeg");
        valueList.add("doc");
        valueList.add("docx");
        valueList.add("csv");
        valueList.add("xlsx");
        valueList.add("xls");
        valueList.add("txt");
        valueList.add("pdf");
        valueList.add("rar");
        valueList.add("zip");
        valueList.add("pptx");
        valueList.add("ppt");
    }

    @Override
    public BaseResult upload(HttpServletRequest request) {
        try {
            String name = request.getParameter("name");
            MultipartHttpServletRequest fileReq = (MultipartHttpServletRequest) request;
            Iterator<String> iter = fileReq.getFileNames();
            JSONObject obj = new JSONObject();
            //此处没有用while的原因，是因为LayUI框架的多文件上传，其实是逐个调用接口上传
            if (iter.hasNext()) {
                MultipartFile reqFile = fileReq.getFile(iter.next());
                if (StringUtils.isNotBlank(name)) {
                    obj.put("name", name);
                } else {
                    obj.put("name", reqFile.getOriginalFilename());
                }
//                if (checkFileType(reqFile.getContentType(), obj.getString("name")))
//                    return new BaseResult(Status.UPLOADFILEEXCEPTION);

                String suffix = reqFile.getOriginalFilename().substring(reqFile.getOriginalFilename().lastIndexOf('.') + 1);
                String fileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
                String path = UPLOAD_PATH + "/" + TimeHelper.getDateYYYY() + "/" + TimeHelper.getDateMM() + "/" + TimeHelper.getDateDay();
                File filePath = new File(path);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                String fileUrl = path + "/" + fileName;
                File file = new File(fileUrl);
                reqFile.transferTo(file);

                obj.put("path", fileUrl.replace(UPLOAD_PATH + "/", ""));
                obj.put("size", getFileSize(file.length()));
            }
            return new BaseResult(Status.UPLOADSUCCESS, obj);
        } catch (Exception e) {
            return new BaseResult(Status.UPLOADFAIL);
        }
    }

    public static void main(String[] args) {
        String key = "bkU0P82xcoa94F04-1614304736";
        String path = "0vKiGrtGRFoedhYCQvx+yA==";
        System.out.println(AesUtil.deCode(path, key));
    }

    @Override
    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine, String fName, String kk) throws Exception {
        //判断kk值是否存在
         String xx = filePath;
        if (!map.containsKey(kk)) {
            returnStatus(response, Status.DOWNLOADKEYFAIL);
            return;
        }
        map.remove(kk);
        String realPath = UPLOAD_PATH + "/";
        if (StringUtils.isBlank(filePath)) {
            returnStatus(response, Status.DOWNLOADFILENOTEXIST);
            return;
        }
        filePath = xx;
        filePath = filePath.replaceAll(" ", "+");
        filePath = AesUtil.deCode(filePath, kk);
        File f = new File(realPath + filePath);
        if (!f.exists()) {
            returnStatus(response, Status.DOWNLOADFILENOTEXIST);
            return;
        }
        String fileName = f.getName();
        fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        if (StringUtils.isNotBlank(fName)) fileName = fName;
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + realPath + filePath);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }

    @Override
    public BaseResult getKey(Integer num, HttpServletRequest request) {
        if (num == null) {
            String key = getGUID() + "-" + TimeHelper.getCurrentTime10();
            map.put(key, request.getSession().getId());
            return BaseResult.success(key);
        } else {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                String key = getGUID();
                map.put(key, request.getSession().getId());
                list.add(key);
            }
            return BaseResult.returnResult(list);
        }
    }


    //10分钟清楚一次过期key
    @Override
    public void removeTimeOutKey() {
        if (map.size() == 0) return;
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            String[] split = key.split("-");
            if ((Long.valueOf(split[1]).intValue() + 600) > TimeHelper.getCurrentTime10()) ite.remove();
        }
    }


    @Override
    public BaseResult uploadVideo(HttpServletRequest request) {
        try {
            String name = request.getParameter("name");
            MultipartHttpServletRequest fileReq = (MultipartHttpServletRequest) request;
            Iterator<String> iter = fileReq.getFileNames();
            JSONObject obj = new JSONObject();
            //此处没有用while的原因，是因为LayUI框架的多文件上传，其实是逐个调用接口上传
            if (iter.hasNext()) {
                MultipartFile reqFile = fileReq.getFile(iter.next());
                if (StringUtils.isNotBlank(name)) {
                    obj.put("name", name);
                } else {
                    obj.put("name", reqFile.getOriginalFilename());
                }
//                if (checkFileType(reqFile.getContentType(), obj.getString("name")))
//                    return new BaseResult(Status.UPLOADFILEEXCEPTION);

                String suffix = reqFile.getOriginalFilename().substring(reqFile.getOriginalFilename().lastIndexOf('.') + 1);
                String fileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
                String path = VIDEO_PATH + "/" + TimeHelper.getDateYYYY() + "/" + TimeHelper.getDateMM() + "/" + TimeHelper.getDateDay();
                File filePath = new File(path);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                String fileUrl = path + "/" + fileName;
                File file = new File(fileUrl);
                reqFile.transferTo(file);

                obj.put("path", fileUrl.replace(VIDEO_PATH + "/", ""));
                obj.put("size", getFileSize(file.length()));
            }
            return new BaseResult(Status.UPLOADSUCCESS, obj);
        } catch (Exception e) {
            return new BaseResult(Status.UPLOADFAIL);
        }
    }

    @Override
    public void showPic(String filePath, HttpServletResponse response) throws Exception {
        String realPath = UPLOAD_PATH + "/";
        if (StringUtils.isBlank(filePath)) {
            returnStatus(response, Status.DOWNLOADFILENOTEXIST);
            return;
        }
        File f = new File(realPath + filePath);
        if (!f.exists()) {
            returnStatus(response, Status.DOWNLOADFILENOTEXIST);
            return;
        }
        String fileName = f.getName();
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要

        URL u = new URL("file:///" + realPath + filePath);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType(u.openConnection().getContentType());
        response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        // 文件名应该编码成UTF-8
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }

    @Override
    public void showVideo(String filePath, HttpServletResponse response) throws IOException {
        String realPath = VIDEO_PATH + "/";
        if (StringUtils.isBlank(filePath)) {
            returnStatus(response, Status.DOWNLOADFILENOTEXIST);
            return;
        }
        File f = new File(realPath + filePath);
        if (!f.exists()) {
            returnStatus(response, Status.DOWNLOADFILENOTEXIST);
            return;
        }
        String fileName = f.getName();
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要

        URL u = new URL("file:///" + realPath + filePath);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType(u.openConnection().getContentType());
        response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        // 文件名应该编码成UTF-8
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }


    private String getFileSize(long length) {

        BigDecimal a = new BigDecimal(length);
        BigDecimal b = new BigDecimal(1024);

        BigDecimal d1 = a.divide(b, 1, BigDecimal.ROUND_HALF_UP);//KB
        if (d1.doubleValue() > 1000) {
            BigDecimal d2 = d1.divide(b, 1, BigDecimal.ROUND_HALF_UP);//MB
            return d2.toString() + "MB";
        }
        return d1.toString() + "KB";

    }


    private boolean checkFileType(String contentType, String name) {
        name = name.substring(name.lastIndexOf(".") + 1);
        if (typeList.contains(contentType) && valueList.contains(name)) return false;
        return true;
    }


    public static String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    private void returnStatus(HttpServletResponse response, Status status) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(new BaseResult(status).toJson());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
