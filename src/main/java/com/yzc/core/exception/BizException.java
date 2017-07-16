package com.yzc.core.exception;

import com.yzc.exception.extendExceptions.WafException;
import com.yzc.support.ErrorMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * 业务异常
 *
 */
public class BizException extends WafException {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * code 为 "WAF/INTERNAL_SERVER_ERROR";
     * HttpStatus 为 HttpStatus.INTERNAL_SERVER_ERROR (500);
     * message 为 message
     * @param message 错误消息描述
     */
    public BizException(String message) {
        super("ACE/INTERNAL_SERVER_ERROR",message,HttpStatus.INTERNAL_SERVER_ERROR);
        logger.error("Request Fail,HttpCode:500,message:"+message,this);
    }

    public BizException(HttpStatus status, String code, String message) {
        super(code, message, status);

        logger.error(String.format("Request Fail,HttpCode:%s,message:%s",status.toString(),message),this);
    }

    public BizException(String message, Exception e) {
        super("ACE/INTERNAL_SERVER_ERROR",message,HttpStatus.INTERNAL_SERVER_ERROR);
        logger.error("Request Fail,HttpCode:500,message:"+message,e);
    }

    public BizException(HttpStatus status, String code, String message, Exception e) {
        super(code,message,status);
        logger.error(String.format("Request Fail,HttpCode:%s,message:%s",status.toString(),message),e);
    }

    public BizException(HttpStatus status,ErrorMessageMapper errorCode){
        super(errorCode.getCode(),errorCode.getMessage(),status);
        logger.error(String.format("Request Fail,HttpCode:%s,message:%s",status.toString(),errorCode.getMessage()),this);
    }

    public BizException(HttpStatus status,ErrorMessageMapper errorCode, Exception e){
        super(errorCode.getCode(),errorCode.getMessage(),status);
        logger.error(String.format("Request Fail,HttpCode:%s,message:%s",status.toString(),errorCode.getMessage()),e);
    }
}
