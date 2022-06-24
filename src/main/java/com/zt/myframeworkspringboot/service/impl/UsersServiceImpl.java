package com.zt.myframeworkspringboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zt.myframeworkspringboot.common.constant.Constants;
import com.zt.myframeworkspringboot.common.exception.CustomException;
import com.zt.myframeworkspringboot.common.util.AesUtil;
import com.zt.myframeworkspringboot.common.util.JwtUtil;
import com.zt.myframeworkspringboot.common.util.TimeHelper;
import com.zt.myframeworkspringboot.entity.Role;
import com.zt.myframeworkspringboot.entity.UserRole;
import com.zt.myframeworkspringboot.entity.Users;
import com.zt.myframeworkspringboot.mapper.UserRoleMapper;
import com.zt.myframeworkspringboot.mapper.UsersMapper;
import com.zt.myframeworkspringboot.param.LoginParam;
import com.zt.myframeworkspringboot.param.ResetPWParam;
import com.zt.myframeworkspringboot.security.config.SecurityProperties;
import com.zt.myframeworkspringboot.security.core.SecurityUserInfo;
import com.zt.myframeworkspringboot.service.MenuService;
import com.zt.myframeworkspringboot.service.RoleService;
import com.zt.myframeworkspringboot.service.UsersService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.zt.myframeworkspringboot.common.base.BaseParam;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.common.status.Status;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author
 * @since 2022-03-20
 */

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    private UserRoleMapper userRoleMapper;


    @Override
    public BaseResult getUsersPage(BaseParam param){
        Page<Users> page = lambdaQuery()
                .select(Users.class,v->!v.getColumn().equals("password"))
                .like(StringUtils.isNotBlank(param.getName()),Users::getNickname,param.getName())
                .like(StringUtils.isNotBlank(param.getPhone()),Users::getPhone,param.getPhone())
                .eq(param.getStatus()!=null,Users::getStatus,param.getStatus())
                .orderBy(true,param.getIsASC()!=null?param.getIsASC():false, Users::getCreateTime)
                .page(param.getPage(entityClass));
        return BaseResult.returnResult(page);
    }

    @Override
    public BaseResult<Users> getUsersOne(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        Users users = getById(param.getId());
        if(users!=null){
            users.setPassword(null);
            users.setRoleList(roleService.getRoleByUser(users.getId()));
        }
        return BaseResult.returnResult(users);
    }

    @Override
    public BaseResult<Boolean> addUsers(Users users){
        if(count(Wrappers.lambdaQuery(entityClass).eq(Users::getLoginName,users.getLoginName()))>0) return BaseResult.error("账号已存在");
        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        save(users);
        List<Long> roleIds = users.getRoleIds();
        if(roleIds!=null&&users.getId()!=null){
            roleIds.forEach(v->{
                if(userRoleMapper.insert(new UserRole(users.getId(),v))!=1) throw new CustomException(Status.OPFAIL);
            });
        }
        return BaseResult.returnResult(users.getId()!=null,users.getId());
    }

    @Override
    public BaseResult<Boolean> updateUsers(Users users){
        if (users.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        if(count(Wrappers.lambdaQuery(entityClass).eq(Users::getLoginName,users.getLoginName()).ne(Users::getId,users.getId()))>0) return BaseResult.error("账号已存在");
        users.setPassword(null);
        List<Long> roleIds = users.getRoleIds();
        if(roleIds!=null){
            userRoleMapper.delete(Wrappers.lambdaQuery(UserRole.class).eq(UserRole::getUserId,users.getId()));
            roleIds.forEach(v->{
                if(userRoleMapper.insert(new UserRole(users.getId(),v))!=1) throw new CustomException(Status.OPFAIL);
            });
        }
        boolean b = updateById(users);
        if(b) reloadUserCache(users.getId());
        return BaseResult.returnResult(b);
    }

    @Override
    public BaseResult<Boolean> delUsers(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        boolean b = removeById(param.getId());
        if(b) reloadUserCache(param.getId());
        return BaseResult.returnResult(b);
    }

    @Override
    public BaseResult<Boolean> bathDelUsers(BaseParam param){
        if (param.getIds() == null || param.getIds().isEmpty()) return BaseResult.error(Status.PARAMEXCEPTION);
        boolean b = removeByIds(param.getIds());
        if(b) param.getIds().forEach(this::reloadUserCache);
        return BaseResult.returnResult(b);
    }

    @Override
    public BaseResult<String> login(LoginParam param) {
        String captchaKey = Constants.CAPTCHA_CODE_KEY.getName() + param.getKey();
        Object realCode = redisTemplate.opsForValue().get(captchaKey);
        if(realCode==null) return new BaseResult(Status.LOGIN_FAIL_CAUSE_CODE_INVALID);
        if(!((String)realCode).equalsIgnoreCase(param.getCode())) return new BaseResult(Status.LOGINFAILCAUSECODE);
        redisTemplate.delete(captchaKey);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(param.getLoginName(), param.getPassword()));
        SecurityUserInfo securityUserInfo = (SecurityUserInfo)authentication.getPrincipal();
        Users users = securityUserInfo.getUserInfo();
        if(!bCryptPasswordEncoder.matches(param.getPassword(),users.getPassword())) return new BaseResult(Status.LOGINFAILCAUSEPWD);
        if(users.getStatus()==0) return new BaseResult(Status.LOGINFAILCAUSEBAN);
        users.setPassword(null);
        String token = AesUtil.enCode(users.getId().toString()) + "-" + AesUtil.enCode(TimeHelper.getCurrentTime10Str());
        String jwtToken = JwtUtil.createToken(token);
        securityUserInfo.setToken(jwtToken);
        securityUserInfo.setLoginTime(TimeHelper.getCurrentTime10());
        securityUserInfo.setPlatform(0);//pc登录
        redisTemplate.opsForValue().set(token, JSON.toJSONString(securityUserInfo), securityProperties.getTimeOut() ,TimeUnit.MINUTES);
        //设置到security上下文
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUserInfo, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return BaseResult.returnResult(Status.LOGINSUCCESS,jwtToken);
    }

    @Override
    public BaseResult<Boolean> resetPassword(ResetPWParam param) {
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
//        Users users1 = getById(param.getId());
//        if(!bCryptPasswordEncoder.matches(param.getPassword(),users1.getPassword())) return BaseResult.error("原密码错误");
        Users users = new Users();
        users.setId(param.getId());
        users.setPassword(bCryptPasswordEncoder.encode(param.getNewPassword()));
        return BaseResult.returnResult(updateById(users));
    }

    @Override
    public void reloadUserCache(Long userId) {
        Set<String> keys = redisTemplate.keys(AesUtil.enCode(userId.toString()) + "-*");
        Users users = getById(userId);
        if(users!=null) users.setRoleList(roleService.getRoleByUser(users.getId()));
        keys.forEach(key->{
            Long expire = redisTemplate.getExpire(key);
            if(expire>0){
                String s = (String)redisTemplate.opsForValue().get(key);
                SecurityUserInfo securityUserInfo = JSONObject.parseObject(s, SecurityUserInfo.class);
                if(users==null || users.getStatus()==0) {
                    redisTemplate.delete(key);
                    return;
                }
                securityUserInfo.setUserInfo(users);
                securityUserInfo.setPermissions(menuService.getPermsByUser(users.getId()));
                redisTemplate.opsForValue().set(key,JSON.toJSONString(securityUserInfo),expire/60,TimeUnit.MINUTES);
            }
        });
    }

    @Override
    public SecurityUserInfo verifyTokenAndRefresh(String token) {
        String verifyToken = JwtUtil.verifyToken(token);
        SecurityUserInfo securityUserInfo = null;
        String s = verifyToken!=null?(String)redisTemplate.opsForValue().get(verifyToken):null;
        if(StringUtils.isNotBlank(s)) {
            securityUserInfo = JSONObject.parseObject(s,SecurityUserInfo.class);
            if((TimeHelper.getCurrentTime10() - securityUserInfo.getLoginTime()) > 3600) redisTemplate.expire(verifyToken,securityProperties.getTimeOut(), TimeUnit.MINUTES);
        }
        return securityUserInfo;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(JwtUtil.verifyToken(token));
    }

}
