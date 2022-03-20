package com.zt.myframeworkspringboot.common.constant;


public enum Constants {

    CAPTCHA_CODE_KEY("captcha_codes:");

    private Constants(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }


}
