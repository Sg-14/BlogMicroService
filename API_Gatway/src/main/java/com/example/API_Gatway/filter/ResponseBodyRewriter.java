package com.example.API_Gatway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class ResponseBodyRewriter implements RewriteFunction<String, String> {

    @Override
    public Publisher<String> apply(ServerWebExchange serverWebExchange, String responseBody) {
        log.info("Outgoing response for url: {}", serverWebExchange.getRequest().getURI().getPath());
        System.out.println(responseBody);
        if(!Strings.isBlank(responseBody) ) {
            if (responseBody.equals("[]")){
                return Mono.just("No comments for this post");
            }
            return Mono.just(responseBody);
        }
        else {
            return Mono.just(StringUtils.EMPTY);
        }
    }
}
