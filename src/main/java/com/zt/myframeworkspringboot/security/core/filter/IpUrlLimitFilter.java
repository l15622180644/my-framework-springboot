package com.zt.myframeworkspringboot.security.core.filter;

import com.zt.myframeworkspringboot.common.status.Status;
import com.zt.myframeworkspringboot.common.util.IpUtils;
import com.zt.myframeworkspringboot.common.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author
 * @module
 * @date 2022/6/15 9:12
 */
public class IpUrlLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(IpUrlLimitFilter.class);

    RedisTemplate<String,Object> redisTemplate;

    private static final String LOCK_IP_URL_KEY = "lock:ip_url_limit:";

    private static final String IP_URL_REQ_TIME="lock:ip_url_times:";

    private static final long LIMIT_TIMES = 15;//单位时间内访问上限

    private static final int UNIT_TIME = 1;//单位时间，单位：秒

    private static final int IP_LOCK_TIME = 60;//ip锁定时长，单位：秒

    public IpUrlLimitFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ipAddr = IpUtils.getIpAddr(request);
        log.info("请求【IP】： {}  【URI】 ------------------------ {}", ipAddr, request.getRequestURI());
        if(ipIsLock(ipAddr)) {
            ServletUtil.returnJSON(response, Status.REQUEST_TOO_FREQUENT);
            return;
        }
        if(!addRequestTime(ipAddr,request.getRequestURI())) {
            ServletUtil.returnJSON(response, Status.REQUEST_TOO_FREQUENT);
            return;
        }
        filterChain.doFilter(request,response);
    }

    private boolean ipIsLock(String ip){
        return redisTemplate.hasKey(LOCK_IP_URL_KEY + ip);
    }

    private boolean addRequestTime(String ip,String uri){
        String key = IP_URL_REQ_TIME + ip + "_" + uri;
        if(redisTemplate.hasKey(key)){
            Long increment = redisTemplate.opsForValue().increment(key);
            if(increment>LIMIT_TIMES){
                getLock(LOCK_IP_URL_KEY + ip, ip, IP_LOCK_TIME);
                return false;
            }
        } else {
            getLock(key,1,UNIT_TIME);
        }
        return true;
    }

    private boolean getLock(String lockKey, Object value, int expireTime){
        //使用lua脚本保证原子性，防止死锁
        String script = "if (redis.call('SETNX',KEYS[1],ARGV[1])==1) then return redis.call('EXPIRE',KEYS[1],ARGV[2]); else return false; end;";
        return redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(lockKey), value, expireTime);
    }

}
