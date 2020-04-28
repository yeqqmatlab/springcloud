package com.liveinpast.stress.gateway.config.exception;

import com.google.common.collect.Maps;
import com.liveinpast.stress.common.StressResult;
import com.liveinpast.stress.common.exception.TokenValidException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 网关(webflux)全局异常处理
 *
 * @author Live.InPast
 * @date 2018/10/31
 */
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resourceProperties the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     */
    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.OK.value();
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> errors = Maps.newHashMap();
        Throwable error = this.getError(request);
        if (error instanceof TokenValidException) {
            //密钥异常
            errors.put("errCode", "10000");
        } else if (error instanceof ResponseStatusException) {
            errors.put("errCode", String.valueOf(((ResponseStatusException) error).getStatus().value()));
        } else {
            errors.put("errCode", StressResult.FAIL);
        }
        errors.put("errMsg", error.getMessage());
        errors.put("data", null);
        return errors;
    }
}
