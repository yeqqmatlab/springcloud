package com.liveinpast.stress.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.liveinpast.stress.common.StressResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 公共过滤器
 *
 * @author Live.InPast
 * @date 2018/11/20
 */
public class BaseFilter {

    /**
     * 自定义过滤器顺序
     */
    public static final int CUS_FILTER_ORDER_1 = -10;
    public static final int CUS_FILTER_ORDER_2 = -9;
    public static final int CUS_FILTER_ORDER_3 = -8;
    public static final int CUS_FILTER_ORDER_4 = -7;
    public static final int CUS_FILTER_ORDER_5 = -7;

    /**
     * 是否是白名单
     */
    public static final String IS_WHITE_LIST_KEY = "IsWhiteList";


    /**
     * Token验证失败
     *
     * @return
     */
    protected Mono<Void> response(ServerWebExchange exchange, String message) {
        //没有权限、
        return response(exchange, "01", message);
    }

    /**
     * Token验证失败
     *
     * @return
     */
    protected Mono<Void> response(ServerWebExchange exchange, String code, String message) {
        //没有权限
        String result = JSONObject.toJSONString(StressResult.fail(code).msg(message), SerializerFeature.WriteMapNullValue);
        DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=utf-8");
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

}
