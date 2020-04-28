package com.liveinpast.stress.gateway;

import com.liveinpast.stress.gateway.config.properties.LabelRuleProperties;
import com.liveinpast.stress.gateway.config.properties.WhiteListProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关启动点
 *
 * @author: Live.InPast
 * @date: 2020/4/12
 */
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.liveinpast")
@EnableConfigurationProperties({
        WhiteListProperties.class,
        LabelRuleProperties.class
})
public class GatewayApplication {

    public static void main(String[] args) {
        new SpringApplication(GatewayApplication.class).run(args);
    }

}
