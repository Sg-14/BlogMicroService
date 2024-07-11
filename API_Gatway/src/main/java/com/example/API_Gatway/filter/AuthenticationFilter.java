package com.example.API_Gatway.filter;

import com.example.API_Gatway.clients.AuthClient;
import com.example.API_Gatway.utils.JwtTokenProvider;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    AuthClient authClient;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public AuthenticationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if(routeValidator.isSecure.test(exchange.getRequest())){
                // header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("Missing Authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().
                        get(HttpHeaders.AUTHORIZATION)
                        .get(0);
                if(StringUtils.hasText(authHeader) && authHeader.contains("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                try {
                    // REST call to auth service
                    jwtTokenProvider.validateToken(authHeader);
                } catch (Exception e){
                    throw new RuntimeException("Unauthorized access to application");
                }

            }
            return chain.filter(exchange);
        }));
    }

    public static class Config{

    }
}
