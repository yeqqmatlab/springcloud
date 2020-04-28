package com.liveinpast.stress.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 熔断降级
 *
 * @author: Live.InPast
 * @date: 2020/4/12
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Test服务
     *
     * @return
     */
    @RequestMapping(value = "/stress-test-service", produces = "application/json;charset=utf-8")
    public String test() {
        return "Stress Test服务异常";
    }

}
