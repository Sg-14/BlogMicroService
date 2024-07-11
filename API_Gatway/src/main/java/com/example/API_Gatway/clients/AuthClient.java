package com.example.API_Gatway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AUTHENTICATION")
public interface AuthClient {

    @GetMapping("/validate")
    String validateToken(@RequestParam("token") String token);
}
