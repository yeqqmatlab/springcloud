package com.liveinpast.stress.common.exception;

import com.liveinpast.stress.common.StressResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author: Live.InPast
 * @Date: 2020/4/20
 */
@RestControllerAdvice
@ConditionalOnClass(WebMvcConfigurationSupport.class)
public class GlobalExceptionConfig {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    /**
     * 业务异常
     */
    @ExceptionHandler
    @ResponseBody
    public StressResult handleTokenValidException(TokenValidException e) {
        LOG.warn("业务异常:", e.getMessage());
        return StressResult.fail("10000").msg("重新登录");
    }

    /**
     * 拦截请求方法错误异常
     *
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public StressResult handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return StressResult.fail().msg(String.format("该请求不支持%s方法",e.getMethod()));
    }

    /**
     * 拦截404异常
     *
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public StressResult handle404Exception(NoHandlerFoundException e) {
        return StressResult.fail().msg("请求地址不存在.");
    }

    /**
     * 拦截没有特殊处理的异常
     *
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public StressResult handleException(Exception e) {
        LOG.error("系统异常:", e);
        return StressResult.fail().msg("系统开小差了,请稍后再试试吧.");
    }

}
