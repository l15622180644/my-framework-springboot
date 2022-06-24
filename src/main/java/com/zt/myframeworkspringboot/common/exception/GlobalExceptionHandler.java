package com.zt.myframeworkspringboot.common.exception;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.zt.myframeworkspringboot.common.base.BaseResult;
import com.zt.myframeworkspringboot.common.status.Status;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 拦截 自定义异常 报出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = CustomException.class)
    public BaseResult serviceException(CustomException e){
        LOGGER.error(CustomException.class.getName(), e);
        return new BaseResult(e.getCode(),e.getMsg());
    }

    /**
     * 拦截 json解析 报出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public BaseResult exceptionHandler(HttpMessageNotReadableException e) {
        LOGGER.error(HttpMessageNotReadableException.class.getName(), e);
        return new BaseResult(Status.PARAMEXCEPTION,Status.PARAMEXCEPTION.getMsg() + "，原因："+e.getCause().getMessage());
    }

    /**
     * 拦截 请求方法不匹配 报出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BaseResult exceptionHandler(HttpRequestMethodNotSupportedException e) {
        LOGGER.error(HttpRequestMethodNotSupportedException.class.getName(), e);
        return new BaseResult(Status.METHOD_NOT_ALLOWED,"该请求仅支持"+ Arrays.toString(e.getSupportedMethods()));
    }

    /**
     * 拦截 对于get请求缺少必传参数 报出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public BaseResult exceptionHandler(MissingServletRequestParameterException e) {
        LOGGER.error(MissingServletRequestParameterException.class.getName(), e);
        return new BaseResult(Status.PARAMEXCEPTION,"缺少类型为 "+e.getParameterType()+" 的 "+e.getParameterName()+" 参数");
    }

    /**
     * 拦截 由@Validated 报出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResult exceptionHandler(MethodArgumentNotValidException e){
        LOGGER.error(MethodArgumentNotValidException.class.getName(), e);
        String msg = e.getBindingResult().getFieldErrors().stream().map(err->{
            return err.getField() + err.getDefaultMessage();
        }).collect(Collectors.joining("；"));
        return new BaseResult(Status.PARAMEXCEPTION,Status.PARAMEXCEPTION.getMsg() + "，原因：" +msg);
    }

    /**
     * 拦截 上传文件大小超出 报出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public BaseResult exceptionHandler(MaxUploadSizeExceededException e){
        LOGGER.error(MaxUploadSizeExceededException.class.getName(), e);
        return new BaseResult(Status.UPLOADFAIL);
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResult exceptionHandler(Exception e){
        LOGGER.error(Exception.class.getName(), e);
        return new BaseResult(Status.EXCEPTION);
    }
}
