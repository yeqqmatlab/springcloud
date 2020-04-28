package com.liveinpast.stress.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.liveinpast.stress.common.TokenVerifyReqDTO;
import com.liveinpast.stress.common.TokenVerifySuccessResDTO;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * 4
 * Token验证
 *
 * @author Live.InPast
 * @date 2018/10/22
 */
@Component
public class TokenHandleFilter extends BaseFilter implements GlobalFilter, Ordered {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(TokenHandleFilter.class);

    /**
     * 请求头部Authorization
     */
    private static final String HEADER_AUTHORIZATION_KEY = "Authorization";

    /**
     * Authorization分两段(Bearer xxx或Basic xxx)
     */
    public static final int TOKEN_SPLIT_SIZE = 2;

    /**
     * Basic验证头部
     */
    public static final String BASIC = "Basic";

    /**
     * Jwt Token验证头部
     */
    public static final String BEARER = "Bearer";

    @Resource
    private HttpClient httpClient;

    @Resource
    private LoadBalancerClient loadBalancerClient;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Boolean isWhiteList = exchange.getAttribute(IS_WHITE_LIST_KEY);
        if (isWhiteList != null && isWhiteList) {
            return chain.filter(exchange);
        } else {
            //判断请求头部是否包含Authorization
            if (!exchange.getRequest().getHeaders().containsKey(HEADER_AUTHORIZATION_KEY)) {
                return response(exchange, "您需要得到授权后才能访问此接口");
            }

            String authorizationValue = exchange.getRequest().getHeaders().getFirst(HEADER_AUTHORIZATION_KEY);
            if (Strings.isNullOrEmpty(authorizationValue)) {
                return response(exchange, "您需要得到授权后才能访问此接口");
            }
            String[] authorizations = authorizationValue.trim().split(" ");
            if (authorizations.length != TOKEN_SPLIT_SIZE || Strings.isNullOrEmpty(authorizations[1])) {
                return response(exchange, "您需要得到授权后才能访问此接口");
            }
            if (!BASIC.equals(authorizations[0].trim()) && !BEARER.equals(authorizations[0].trim())) {
                return response(exchange, "您需要得到授权后才能访问此接口");
            }

            String authType = authorizations[0].trim();
            String authValue = authorizations[1].trim();

            if (LOG.isDebugEnabled()) {
                LOG.debug("authType值:{},authValue值:{}", authType, authValue);
            }


            //通过轮训查找服务实例
            ServiceInstance serviceInstance = loadBalancerClient.choose("stress-auth-service");
            if (serviceInstance == null) {
                throw new RuntimeException("找不到stress-auth-service实例");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("stress-auth-service地址是:{}", serviceInstance.getUri());
            }


            TokenVerifyReqDTO tokenVerifyReqDTO = new TokenVerifyReqDTO();
            tokenVerifyReqDTO.setToken(authValue);

            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

            if (authType.equals(BEARER)) {

                //要加上熔断的功能
                return httpClient
                        .headers(headers->headers.add("Content-Type","application/json;charset=utf-8"))
//                        .tcpConfiguration(tcpClient -> tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000))
                        .request(HttpMethod.POST)
                        .uri(serviceInstance.getUri() + "/authorization/verify-jwt2")
                        //The data send to auth service api
                        .send(ByteBufFlux.fromString(Mono.just(JSONObject.toJSONString(tokenVerifyReqDTO))))
                        .responseSingle((resp, bytes) -> bytes.asString())
                        .flatMap(resultStr -> {
                            JSONObject result = JSONObject.parseObject(resultStr);
                            String errCode = result.getString("errCode");
                            if (errCode != null && errCode.equals("00")) {
                                TokenVerifySuccessResDTO tokenVerifySuccessResDTO = JSONObject.parseObject(result.getString("data"), TokenVerifySuccessResDTO.class);
                                exchange.getAttributes().put("userId", tokenVerifySuccessResDTO.getUserId());
                                exchange.getAttributes().put("extraUserId", tokenVerifySuccessResDTO.getExtraUserId());
                                exchange.getAttributes().put("openId", tokenVerifySuccessResDTO.getOpenId());
                                exchange.getAttributes().put("unionId", tokenVerifySuccessResDTO.getUnionId());
                                exchange.getAttributes().put("schoolId", tokenVerifySuccessResDTO.getSchoolId().toString());
                                exchange.getAttributes().put("serviceGroup", tokenVerifySuccessResDTO.getServiceGroup());
                                exchange.getAttributes().put("service", route.getId());
                                exchange.getAttributes().put("client", "pc");
                                exchange.getAttributes().put("clientName", tokenVerifySuccessResDTO.getClientName());
                               /* ServerHttpRequest request = exchange.getRequest().mutate()
                                .header("userId", tokenVerifySuccessResDTO.getUserId())
                                .header("extraUserId", tokenVerifySuccessResDTO.getExtraUserId())
                                .header("openId", tokenVerifySuccessResDTO.getOpenId())
                                .header("unionId", tokenVerifySuccessResDTO.getUnionId())
                                .header("schoolId", tokenVerifySuccessResDTO.getSchoolId().toString())
                                .header("serviceGroup", tokenVerifySuccessResDTO.getServiceGroup())
                                .header("service", route.getId())
                                .header("client", "pc")
                                .header("clientName", tokenVerifySuccessResDTO.getClientName()).build();
                                return chain.filter(exchange.mutate().request(request).build());*/
                                return chain.filter(exchange);
                            } else {
                                return response(exchange, result.getString("errCode"), result.getString("errMsg"));
                            }
                        });
            } else {
                return response(exchange, "01", "不支持Basic驗證");
            }
        }
    }

    @Override
    public int getOrder() {
        return CUS_FILTER_ORDER_4;
    }
}
