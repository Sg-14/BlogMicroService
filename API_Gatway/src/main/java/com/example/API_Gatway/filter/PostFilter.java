package com.example.API_Gatway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PostFilter extends AbstractGatewayFilterFactory<PostFilter.Config> {

    public PostFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(()->{
            log.info("****** Intercepted Post Response ******");
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.forEach(
                    (key, value) -> log.info("The key is {} and value is {}", key, value)
            );

            log.info("Response status code is: {}", response.getStatusCode());
        }))));
    }

    public static class Config{

    }
}
