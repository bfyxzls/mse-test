package com.pkulaw.permission;

import com.pkulaw.config.UserContextHolder;
import com.pkulaw.exception.AuthException;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 权限拦截
 *
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class PermissionFilter implements WebFilter {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private Mono<Void> redirectToNewUrl(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().set(HttpHeaders.LOCATION, "/home/login");
        return response.setComplete();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return requestMappingHandlerMapping.getHandler(exchange).switchIfEmpty(chain.filter(exchange)).flatMap(handler -> {
            try {
                String path = exchange.getRequest().getPath().value();
                if (path.contains("/home/login")) {
                    // 如果是前端资源，则直接跳过，不做任何处理
                    return exchange.getResponse().setComplete();
                }

                HandlerMethod method = (HandlerMethod) handler;
                PermissionLimit permissionLimit = method.getMethodAnnotation(PermissionLimit.class);
                if (permissionLimit == null) {
                    // 如果是前端资源，则直接跳过，不做任何处理
                    return exchange.getResponse().setComplete();
                }

                boolean needAdminuser = permissionLimit.adminuser();
                String[] permissions = permissionLimit.permissions();
                LoginUser loginUser = UserContextHolder.get();
                if (loginUser == null) {
                    return Mono.error(new AuthException("missing token", 401));
                }
                if (needAdminuser && loginUser.getRoles() != "1") {
                    return Mono.error(new AuthException("need admin user"));
                }
                if (!loginUser.validPermission(permissions)) {
                    return Mono.error(new AuthException("permission forbidden"));
                }
                return chain.filter(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Mono.empty();
        });
    }
}
