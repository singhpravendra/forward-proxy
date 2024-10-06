package com.pravendra.server.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ServerController {
    private static final String PROXY_IP = "127.0.0.1"; // Replace with your proxy's actual IP address

    @GetMapping(value = "/hello")
    public Mono<String> hello(ServerWebExchange exchange) {
        String proxyIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        var clientIp = exchange.getRequest().getHeaders().get("X-Forwarded-For");

        /*if (!clientIp.equals(PROXY_IP)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }*/
        return Mono.just("Hello from the Proxy Server :" + proxyIp + ", ClientIP: " + clientIp);
    }
}
