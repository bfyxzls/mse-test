package com.pkulaw.permission;

import com.pkulaw.config.UserContextHolder;
import org.apache.http.HttpException;
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
                    return chain.filter(exchange);
                }

                HandlerMethod method = (HandlerMethod) handler;
                PermissionLimit permissionLimit = method.getMethodAnnotation(PermissionLimit.class);
                if (permissionLimit == null) {
                    return chain.filter(exchange);
                }
                boolean needAdminuser = permissionLimit.adminuser();
                String[]   permissions = permissionLimit.permissions();
                LoginUser loginUser = UserContextHolder.get();
                if (loginUser == null) {
                    return redirectToNewUrl(exchange);
                }
                if (needAdminuser && loginUser.getRoles() != "1") {
                    throw new HttpException("need_admin_user");
                }
                if (!loginUser.validPermission(permissions)) {
                    throw new HttpException("permission_forbidden");
                }
                return chain.filter(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Mono.empty();
        });
    }
}
