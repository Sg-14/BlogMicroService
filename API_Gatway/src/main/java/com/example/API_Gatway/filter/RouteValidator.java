package com.example.API_Gatway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.function.Predicate;


@Component
public class RouteValidator {
    public static final List<String> apiEndPoints = List.of(
            "auth/register",
            "auth/login",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecure =
            request -> apiEndPoints.stream().noneMatch(uri->request.getURI().getPath().contains(uri));
}
