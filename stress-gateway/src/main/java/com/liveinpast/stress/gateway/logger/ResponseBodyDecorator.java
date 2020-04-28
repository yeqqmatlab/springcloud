package com.liveinpast.stress.gateway.logger;

import com.google.common.base.Strings;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;


/**
 * 获取ResponseBody装饰类
 *
 * @author Live.InPast
 * @date 2018/11/26
 */
public class ResponseBodyDecorator extends ServerHttpResponseDecorator {

    private StringBuilder builder = new StringBuilder();

    /**
     * 是否开启gzip压缩
     */
    private boolean isGzip = false;

    /**
     * gzip头部标志
     */
    private static final String CONTENT_ENCODING_GZIP = "gzip";

    public ResponseBodyDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        String contentEncoding = getDelegate().getHeaders().getFirst("Content-Encoding");
        if (!Strings.isNullOrEmpty(contentEncoding) && CONTENT_ENCODING_GZIP.equals(contentEncoding)) {
            isGzip = true;
        }
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
            return super.writeWith(fluxBody.map(dataBuffer -> streamDataBuffer(dataBuffer)));
        }
        if (body instanceof Mono) {
            Mono<? extends DataBuffer> monoBody = (Mono<? extends DataBuffer>) body;
            return super.writeWith(monoBody.map(dataBuffer -> streamDataBuffer(dataBuffer)));
        }
        // if body is not a flux. never got there.
        return super.writeWith(body);
    }

    /**
     * 获取请求返回值
     *
     * @return
     */
    public String getResponseStr() {
        if (isGzip){
            return "开启Gzip,无法解析返回值";
        }
        return builder.toString();
    }

    /**
     * 数据中转
     */
    private DataBuffer streamDataBuffer(DataBuffer dataBuffer) {
        // probably should reuse buffers
        byte[] content = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(content);
        //释放掉内存
        DataBufferUtils.release(dataBuffer);
        builder.append(new String(content, Charset.forName("UTF-8")));
        return getDelegate().bufferFactory().wrap(content);
    }

}
