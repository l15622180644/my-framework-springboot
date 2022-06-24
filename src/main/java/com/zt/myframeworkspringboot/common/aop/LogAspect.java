package com.zt.myframeworkspringboot.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zt.myframeworkspringboot.common.annotation.Log;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.common.util.IpUtils;
import com.zt.myframeworkspringboot.common.util.JwtUtil;
import com.zt.myframeworkspringboot.entity.LogBack;
import com.zt.myframeworkspringboot.entity.Users;
import com.zt.myframeworkspringboot.mapper.LogBackMapper;
import com.zt.myframeworkspringboot.security.core.SecurityUserInfo;
import com.zt.myframeworkspringboot.security.util.MySecurityUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 自定义多数据源事务处理
 *
 * @author lyh
 */
@Aspect
@Component
@Order(1)
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Resource
    private LogBackMapper logBackMapper;

    // 配置织入点
    @Pointcut("@annotation(com.zt.myframeworkspringboot.common.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (null == controllerLog) {
                return;
            }
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            String ipAddr = IpUtils.getIpAddr(request);
            LogBack logBack = new LogBack();
            BaseResult result = (BaseResult) jsonResult;
            if (controllerLog.content().equals("登录")) {
                logBack.setStatus(result.getCode() == Status.LOGINSUCCESS.getCode() ? 0 : 1);
                if (logBack.getStatus() == 1) return;
                SecurityUserInfo securityUserInfo = MySecurityUtil.getUser();
                if (securityUserInfo.getPlatform() == 1) {
                    //微信登录
                    /*logBack.setPlat(1);
                    JSONObject wxUser = re.getJSONObject("wxUserInfo");
                    if (wxUser != null) {
//                        logBack.setUserId(wxUser.getLong("miniOpenId"));
                        logBack.setUserName(wxUser.getString("nickName"));
                    }*/
                } else {
                    //后台登录
                    if(securityUserInfo.getUserInfo()!=null){
                        logBack.setUserName(securityUserInfo.getUserInfo().getNickname());
                        logBack.setUserId(securityUserInfo.getUserInfo().getId());
                    }
                }
            } else {
                logBack.setStatus(result == null ? 1 : result.getCode() == Status.SUCCESS.getCode() ? 0 : result.getCode() == Status.OPSUCCESS.getCode() ? 0 : 1);
                SecurityUserInfo securityUserInfo = MySecurityUtil.getUser();
                if (securityUserInfo.getPlatform() == 1) {
                    //微信用户操作
                    /*logBack.setPlat(1);
                    JSONObject wxUser = userCache.getJSONObject("wxUserInfo");
                    if (wxUser != null) {
//                        logBack.setUserId(wxUser.getLong("miniOpenId"));
                        logBack.setUserName(wxUser.getString("nickName"));
                    }*/
                } else {
                    //后台用户操作
                    if(securityUserInfo.getUserInfo()!=null){
                        logBack.setUserName(securityUserInfo.getUserInfo().getNickname());
                        logBack.setUserId(securityUserInfo.getUserInfo().getId());
                    }
                }
            }
            logBack.setAddr(ipAddr);
            if (logBack.getPlat() == null) logBack.setPlat(checkPlat(request));
            logBack.setModule(controllerLog.module());
//            String perf = controllerLog.type() == Log.INSERT ? "新增" : controllerLog.type() == Log.UPDATE ? "更新" : controllerLog.type() == Log.DELETE ? "删除" : "";
            logBack.setContent(controllerLog.content());
            logBack.setOpp(controllerLog.type());
            logBack.setCreateTime(System.currentTimeMillis() / 1000);
            logBackMapper.insert(logBack);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }


    private int checkPlat(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent.toLowerCase().contains("windows") || userAgent.toLowerCase().contains("macintosh") || userAgent.toLowerCase().contains("postman")) {
            //pc端
            return 0;
        } else {
            //移动端
            return 1;
        }
    }
}
