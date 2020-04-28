package com.liveinpast.stress.gateway.filter;

import com.google.common.base.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * 获取用户请求IP地址
 *
 * @author Live.InPast
 * @date 2018/11/6
 */
@Component
public class RequestIpHandleFilter extends BaseFilter implements GlobalFilter, Ordered {

    /**
     * 本机地址
     */
    private static final String LOCAL_HOST = "127.0.0.1";
    private static final String LOCAL_HOST_ADDRESS = "0:0:0:0:0:0:0:1";
    private static final String UNKNOWN = "unknown";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put("requestIp", getIpAddress(exchange));
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return CUS_FILTER_ORDER_3;
    }


    /**
     * 获取客户端真实IP
     *
     * @return
     */
    private String getIpAddress(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String ip = headers.getFirst("X-Real-IP");
        if (Strings.isNullOrEmpty(ip) || ip.equalsIgnoreCase(UNKNOWN)) {
            ip = headers.getFirst("X-Forwarded-For");
        }
        if (Strings.isNullOrEmpty(ip) || ip.equalsIgnoreCase(UNKNOWN)) {
            ip = headers.getFirst("Proxy-Client-IP");
        } else {
            ip = Arrays.stream(ip.split(",")).filter(ipStr -> !ipStr.equalsIgnoreCase(UNKNOWN)).findAny().orElse(null);
        }
        if (Strings.isNullOrEmpty(ip) || ip.equalsIgnoreCase(UNKNOWN)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (Strings.isNullOrEmpty(ip) || ip.equalsIgnoreCase(UNKNOWN)) {
            ip = getRemoteAddress(exchange);
        }
        if (ip.equals(LOCAL_HOST_ADDRESS)) {
            ip = LOCAL_HOST;
        }
        return ip;
    }

    /**
     * 获取远程地址
     *
     * @return
     */
    private String getRemoteAddress(ServerWebExchange exchange) {
        InetSocketAddress sourceAddress = exchange.getRequest().getRemoteAddress();
        if (sourceAddress == null) {
            return "";
        } else {
            InetAddress address = sourceAddress.getAddress();
            return (address == null ? sourceAddress.getHostString() : address.getHostAddress());
        }
    }

}
