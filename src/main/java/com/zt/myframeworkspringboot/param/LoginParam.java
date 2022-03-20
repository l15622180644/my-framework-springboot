package com.zt.myframeworkspringboot.param;

import com.zt.myframeworkspringboot.common.base.BaseParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginParam extends BaseParam {

    @NotEmpty(message = "账号不能为空")
    private String loginName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "验证码不能为空")
    private String code;

    @NotEmpty(message = "key不能为空")
    private String key;
}
