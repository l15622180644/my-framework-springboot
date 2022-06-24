package com.zt.myframeworkspringboot.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginParam{

    @NotBlank(message = "不能为空")
    private String loginName;

    @NotBlank(message = "不能为空")
    private String password;

    @NotBlank(message = "不能为空")
    private String code;

    @NotBlank(message = "不能为空")
    private String key;
}
