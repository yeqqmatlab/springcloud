package com.liveinpast.stress.eureka;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

/**
 * Eureka注册中心
 *
 * @author: Live.InPast
 * @date: 2020/4/12
 */
@Configuration
@EnableEurekaServer
@EnableAutoConfiguration
public class EurekaApplication {


    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaApplication.class).run(args);
    }

}
