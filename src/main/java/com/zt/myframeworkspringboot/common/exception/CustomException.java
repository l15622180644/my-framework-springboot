package com.zt.myframeworkspringboot.common.exception;


import com.zt.myframeworkspringboot.common.status.Status;

/**
 * 自定义业务逻辑异常
 */
public class CustomException extends RuntimeException{

    /**
     * 业务错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String msg;

    public CustomException(){}

    public CustomException(Integer code,String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public CustomException(Status status){
        super(status.getMsg());
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    public CustomException(String msg){
        super(msg);
        this.code = Status.OPFAIL.getCode();
        this.msg = msg;
    }

    public CustomException(Status status,String msg){
        super(msg);
        this.code = status.getCode();
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
