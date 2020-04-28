package com.liveinpast.stress.test.controller;

import com.liveinpast.stress.common.StressResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Live.InPast
 * @date: 2020/4/12
 */
@RestController
@RequestMapping("/user")
public class UserController {


    /**
     * 测试方法
     *
     * @return
     */
    @GetMapping("/say-hello")
    public StressResult sayHello(HttpServletRequest request) {
        return StressResult.success().data("Hello Stress Test Service for " + request.getHeader("userId"));
    }

    /**
     * 测试方法（白名单）
     *
     * @return
     */
    @GetMapping("/say-hellos")
    public StressResult sayHellos(HttpServletRequest request) {
        return StressResult.success().data("Hello White List Stress Test Service.");
    }

}
