package com.zt.myframeworkspringboot.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    public String content() default "";

    public String module() default "";

    public int type();

    public static final int INSERT = 0;

    public static final int UPDATE = 1;

    public static final int DELETE = 2;

    public static final int CUSTOM = -1;
}
