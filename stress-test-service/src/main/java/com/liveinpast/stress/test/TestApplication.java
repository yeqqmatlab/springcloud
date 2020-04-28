package com.liveinpast.stress.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: Live.InPast
 * @date: 2020/4/12
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.liveinpast")
public class TestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplication.class).run(args);
    }

}
