package com.liveinpast.stress.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.liveinpast.stress.gateway.logger.RequestBodyDecorator;
import com.liveinpast.stress.gateway.logger.ResponseBodyDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 网关日志
 *
 * @author Live.InPast
 * @date 2018/11/26
 */
@Component
public class RequestLogHandleFilter extends BaseFilter implements GlobalFilter, Ordered {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestLogHandleFilter.class);

    /**
     * url分隔符
     */
    public static final String URL_SEPARATOR = "/";

    /**
     * 数字替代字符
     */
    public static final String NUMBER_REPLACE_STR = "?";

    /**
     * 判断url部分中某段是不是纯数字
     */
    public static final String NUMBER_PATTERN_REGEXP = "[0-9]+";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //构建日志
        Map<String, Object> logs = Maps.newConcurrentMap();

        String requestUrl = String.format("%s[%s]", exchange.getRequest().getMethodValue(), exchange.getRequest().getPath().value());
        String requestUri = String.format("%s[%s]", exchange.getRequest().getMethodValue(), convertRequestUri(exchange.getRequest().getPath().value()));

        logs.put("userId", Strings.nullToEmpty(exchange.getAttribute("userId")));
        logs.put("schoolId", Strings.nullToEmpty(exchange.getAttribute("schoolId")));
        logs.put("requestIp", Strings.nullToEmpty(exchange.getAttribute("requestIp")));
        logs.put("requestId", UUID.randomUUID().toString());
        logs.put("requestTime", System.currentTimeMillis());
        logs.put("requestUrl", requestUrl);
        logs.put("requestUri", requestUri);
        logs.put("requestMethod", Strings.nullToEmpty(exchange.getRequest().getMethodValue()));
        logs.put("userAgent", Strings.nullToEmpty(exchange.getRequest().getHeaders().getFirst("User-Agent")));
        //获取请求参数
        RequestBodyDecorator requestBodyDecorator = new RequestBodyDecorator(exchange.getRequest());
        ResponseBodyDecorator responseBodyDecorator = new ResponseBodyDecorator(exchange.getResponse());
        exchange.getAttributes().put("handleTime", System.currentTimeMillis());
        ServerWebExchange serverWebExchange = exchange.mutate()
                .request(requestBodyDecorator)
                .response(responseBodyDecorator)
                .build();
        return chain.filter(serverWebExchange).then(Mono.fromRunnable(() -> {
            logs.put("requestParams", getRequestParams(exchange, requestBodyDecorator));
            Map<String, String> responseMap = getResponseMap(responseBodyDecorator);
            String errCode = responseMap.get("errCode");
            if (!Strings.isNullOrEmpty(errCode)) {
                logs.put("errCode", errCode);
                Long startTime = exchange.getAttribute("handleTime");
                if (startTime != null) {
                    logs.put("handleTime", (System.currentTimeMillis() - startTime));
                }
//                LOG.info(JSONObject.toJSONString(logs));
            }
        }));
    }

    @Override
    public int getOrder() {
        return CUS_FILTER_ORDER_5;
    }


    /**
     * 获取请求参数
     *
     * @param exchange
     * @param requestBodyDecorator
     * @return
     */
    private String getRequestParams(ServerWebExchange exchange, RequestBodyDecorator requestBodyDecorator) {
        String requestParams = "";
        if (exchange.getRequest().getMethod() == HttpMethod.GET) {
            Map<String, String> stringStringMap = exchange.getRequest().getQueryParams().toSingleValueMap();
            //防止被JSONObject转成{},然后日志输出是格式化{}为errCode.
            //See redisLogger.info(JSONObject.toJSONString(logs),logs.get("errCode"))
            if (!stringStringMap.keySet().isEmpty()) {
                requestParams = JSONObject.toJSONString(stringStringMap);
            }
        } else if (requestBodyDecorator.getRecordRequestBody()) {
            requestParams = Strings.nullToEmpty(requestBodyDecorator.getRequestBody());
        }
        return requestParams;
    }

    /**
     * 获取请求返回码
     *
     * @param responseBodyDecorator
     * @return
     */
    private Map<String, String> getResponseMap(ResponseBodyDecorator responseBodyDecorator) {
        Map<String, String> responseMap = Maps.newHashMap();
        String responseStr = Strings.nullToEmpty(responseBodyDecorator.getResponseStr());
        if (responseBodyDecorator.getStatusCode() == null ||
                responseBodyDecorator.getStatusCode() == HttpStatus.OK) {
            if (!Strings.isNullOrEmpty(responseStr)) {
                try {
                    JSONObject jsonObject = JSONObject.parseObject(responseStr);
                    responseMap.put("errCode", jsonObject.getString("errCode"));
                } catch (Exception e) {
                    responseMap.put("errCode", "-1");
                }
            } else {
                responseMap.put("errCode", "-1");
            }
        } else {
            responseMap.put("errCode", String.valueOf(responseBodyDecorator.getStatusCode().value()));
        }
        responseMap.put("response", responseStr);
        return responseMap;
    }

    /**
     * 将请求中的参数以{NUMBER_REPLACE_STR}代替
     *
     * @param requestUrl
     * @return
     */
    private String convertRequestUri(String requestUrl) {
        return Arrays.stream(requestUrl.split(URL_SEPARATOR))
                .filter(c -> c != null && !"".equals(c.trim()))
                .map(c -> {
                    if (c != null && !"".equals(c.trim())) {
                        if (c.matches(NUMBER_PATTERN_REGEXP)) {
                            return NUMBER_REPLACE_STR;
                        }
                    }
                    return c;
                }).collect(Collectors.joining(URL_SEPARATOR, "/", ""));
    }
}
