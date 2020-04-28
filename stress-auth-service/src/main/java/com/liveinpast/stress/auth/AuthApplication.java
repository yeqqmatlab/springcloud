package com.liveinpast.stress.auth;

import com.liveinpast.stress.auth.config.properties.LoginTokenProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: Live.InPast
 * @date: 2020/4/12
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.liveinpast")
@EnableConfigurationProperties({
        LoginTokenProperties.class
})
public class AuthApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthApplication.class).run(args);
    }

}
