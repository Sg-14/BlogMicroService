package com.example.API_Gatway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestBodyFilter implements GlobalFilter, Ordered {

    @Autowired
    ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter;

    @Autowired
    RequestBodyRewriter requestBodyRewriter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return modifyRequestBodyFilter.apply(
                new ModifyRequestBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(String.class, String.class, requestBodyRewriter))
                .filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
