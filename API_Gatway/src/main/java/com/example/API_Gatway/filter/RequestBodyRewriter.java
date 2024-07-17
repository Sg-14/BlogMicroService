package com.example.API_Gatway.filter;

import com.example.API_Gatway.utils.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class RequestBodyRewriter implements RewriteFunction<String, String> {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public Publisher<String> apply(ServerWebExchange serverWebExchange, String requestData) {
        String path = serverWebExchange.getRequest().getURI().getPath();
        HttpMethod method = serverWebExchange.getRequest().getMethod();
        String token = serverWebExchange.getRequest().getHeaders().getFirst("Authorization");
        if(path.contains("auth/login") || path.contains("auth/register")){
            log.info("Path contains login");
            return Mono.just(requestData);
        }
        if(Objects.nonNull(token)){
            token = token.substring(7);
            if(StringUtils.hasText(token)){
                try{
                    jwtTokenProvider.validateToken(token);
                } catch (Exception e){
                    return returnErrorMessage(serverWebExchange,"Invalid Token");
                }
            }
            if(path.contains("/comments") && method.equals(HttpMethod.POST)){
                try {
                    JsonNode jsonNode = objectMapper.readTree(requestData);
                    Map<String, Object> objectMap = objectMapper.readValue(requestData, Map.class);
                    Object name = objectMap.get("name");
                    name = name.toString().toLowerCase();
                    objectMap.put("name", name);
                    String modifiedrequestbody = objectMapper.writeValueAsString(objectMap);
                    log.info("Comment Modified");
                    return Mono.just(modifiedrequestbody);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else if(path.contains("/posts") && method.equals(HttpMethod.POST)){
                try {
                    JsonNode jsonNode = objectMapper.readTree(requestData);
                    Map<String, Object> objectMap= objectMapper.readValue(requestData, Map.class);
                    if(!objectMap.containsKey("categoryId")){
                        objectMap.put("categoryId", 1);
                    }
                    String modifiedresponse = objectMapper.writeValueAsString(objectMap);
                    log.info("Post Modified");
                    return Mono.just(modifiedresponse);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else if(path.contains("/auth/update")){
                String name = jwtTokenProvider.getUsername(token);
                try {
                    Map<String, Object> objectMap = objectMapper.readValue(requestData, Map.class);
                    objectMap.put("username", name);
                    String modifiedResponse = objectMapper.writeValueAsString(objectMap);
                    return Mono.just(modifiedResponse);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(method == HttpMethod.GET){
                return Mono.just("Get Method");
            } else {
                return Mono.just(requestData);
            }
        }
        else {
            return returnErrorMessage(serverWebExchange,"Token Required");
        }
    }

    public Mono<String>  returnErrorMessage(ServerWebExchange exchange, String errorMessage){
        ObjectNode node = objectMapper.createObjectNode();
        node.set("data",null);
        node.put("description", errorMessage);
        node.put("display", true);
        node.put("error",true);
        String jsonErrorMsg=null;
        try {
            jsonErrorMsg = objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonErrorMsg = "Please provide valid token";
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        ServerHttpResponse response1 = exchange.getResponse();
        byte[] bytes = jsonErrorMsg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response1.bufferFactory().wrap(bytes);
        return response1.writeWith(Mono.just(buffer)).then(Mono.empty());
    }

}
