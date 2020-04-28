package com.liveinpast.stress.gateway.logger;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

/**
 * 读取RequestBody的请求装饰类
 *
 * @author Live.InPast
 * @date 2018/11/26
 */
public class RequestBodyDecorator extends ServerHttpRequestDecorator {

    /**
     * 是否记录
     */
    private Boolean recordRequestBody = false;

    /**
     * 请求实体
     */
    private final StringBuilder requestBody = new StringBuilder();

    public RequestBodyDecorator(ServerHttpRequest delegate) {
        super(delegate);
        if (delegate.getMethod() == HttpMethod.POST ||
                delegate.getMethod() == HttpMethod.PUT){
            MediaType contentType = delegate.getHeaders().getContentType();
            //只记录application/json;charset=utf-8和application/x-www-form-urlencoded请求头部
            recordRequestBody =  contentType != null && (contentType.equals(MediaType.APPLICATION_JSON_UTF8) ||
                    contentType.equals(MediaType.APPLICATION_JSON) ||
                    contentType.equals(MediaType.APPLICATION_FORM_URLENCODED));
        }
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(this::resolveBody);
    }


    public Boolean getRecordRequestBody() {
        return recordRequestBody;
    }

    /**
     * 外部获取请求实体
     */
    public String getRequestBody() {
        return requestBody.toString();
    }

    /**
     * 记录请求实体
     * @param buffer
     */
    private void resolveBody(DataBuffer buffer) {
        if (recordRequestBody){
            requestBody.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString());
        }
    }
}
