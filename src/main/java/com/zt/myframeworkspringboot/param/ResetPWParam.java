package com.zt.myframeworkspringboot.param;

import com.zt.myframeworkspringboot.common.base.BaseParam;
import lombok.Data;

@Data
public class ResetPWParam extends BaseParam {

    private String loginName;

    private String password;

    private String newPassword;
}
