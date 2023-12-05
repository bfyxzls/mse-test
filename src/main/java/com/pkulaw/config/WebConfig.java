package com.pkulaw.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkulaw.handler.GlobalExceptionHandler;
import com.pkulaw.handler.HelloHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author lind
 * @date 2023/12/5 11:07
 * @since 1.0.0
 */
@Configuration
public class WebConfig {

    // 添加新的路由hello
    @Bean
    public RouterFunction<ServerResponse> route(HelloHandler helloHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/hello"), helloHandler);
    }

     // 全局异常处理
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }

}
