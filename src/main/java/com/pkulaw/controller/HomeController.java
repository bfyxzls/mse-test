package com.pkulaw.controller;

import com.pkulaw.config.AuthProperties;
import com.pkulaw.permission.PermissionLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @author lind
 * @date 2023/11/28 13:18
 * @since 1.0.0
 */
@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private AuthProperties authProperties;

    @GetMapping("/index")
    public Flux<String> index() {
        return Flux.just("hello world!" + authProperties.toString());
    }

    @GetMapping("/call")
    @PermissionLimit(permissions = {"read", "write"})
    public Flux<String> call() {
        return Flux.just("call page");
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> login() throws IOException {
        String result =
                "<html><head><title>登录页面</title></head><body><h1>登录页面</h1>" +
                        "<form action='/home/post' method='post'><ul>" +
                        "<li>用户名：<input type=\"text\"></li>" +
                        "<li>密码：<input type=\"password\"></li>" +
                        "<li><button>登 录</button></li>" +
                        "</ul></form>" +
                        "</body></html>";
        return Mono.just(result);
    }
}
