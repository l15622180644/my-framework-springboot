package com.zt.myframeworkspringboot.security.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "token")
@Data
@Validated
public class SecurityProperties {

    @NotBlank(message = "token.head 不能为空")
    private String head;

    @NotNull(message = "token.timeOut 不能为空")
    private int timeOut;

}
