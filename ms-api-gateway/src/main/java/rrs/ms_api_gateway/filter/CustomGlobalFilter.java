package rrs.ms_api_gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpCookie authCookie = exchange.getRequest().getCookies().getFirst("AUTH-TOKEN");

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().headers(httpHeaders -> {
            httpHeaders.remove(HttpHeaders.AUTHORIZATION);
            if (authCookie != null) {
                httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + authCookie.getValue());
            }
        }).build();
        
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
