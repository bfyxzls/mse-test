package com.pkulaw.permission;

import com.pkulaw.config.UserContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author lind
 * @date 2023/12/5 11:45
 * @since 1.0.0
 */
@Component
@Order(1000)
public class TokenFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (token != null) {
            LoginUser loginUser = new LoginUser();
            loginUser.setRoles("read");
            UserContextHolder.set(loginUser);
        }
        return chain.filter(exchange);//这个过滤器完成，然后传到下一个过滤器中处理请求

    }
}
