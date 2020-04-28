package com.liveinpast.stress.gateway.filter;

import com.liveinpast.stress.gateway.config.properties.WhiteListProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 处理白名单请求
 *
 * @author Live.InPast
 * @date 2018/10/22
 */
@Component
public class WhiteListHandleFilter extends BaseFilter implements GlobalFilter, Ordered {

    @Resource
    private WhiteListProperties whiteListProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUri = exchange.getRequest().getURI().toString();
        /*return Mono.just(whiteListProperties.getPatterns())
                .doOnNext(patterns -> exchange.getAttributes().put(IS_WHITE_LIST_KEY, patterns.stream().anyMatch(pattern -> requestUri.contains(pattern))))
                .then(chain.filter(exchange));*/
        //不知道哪个好,先留着后面在搞懂原理
        return Mono.just(whiteListProperties.getPatterns()).flatMap(patterns -> {
            exchange.getAttributes().put(IS_WHITE_LIST_KEY, patterns.stream().anyMatch(pattern -> requestUri.contains(pattern)));
            return chain.filter(exchange);
        });
    }

    @Override
    public int getOrder() {
        return CUS_FILTER_ORDER_2;
    }
}
