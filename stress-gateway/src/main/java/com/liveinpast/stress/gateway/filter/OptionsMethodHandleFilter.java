package com.liveinpast.stress.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 处理OPTIONS请求
 * 如果请求方式是OPTIONS,则直接放回204状态码
 *
 * @author Live.InPast
 * @date 2020/04/19
 */
@Component
public class OptionsMethodHandleFilter extends BaseFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getMethodValue().equals(HttpMethod.OPTIONS.name())) {
            exchange.getResponse().setStatusCode(HttpStatus.NO_CONTENT);
            //立即完成请求
            return exchange.getResponse().setComplete();
        } else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return CUS_FILTER_ORDER_1;
    }

}
