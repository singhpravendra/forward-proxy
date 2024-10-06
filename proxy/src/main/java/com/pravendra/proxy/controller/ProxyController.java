package com.pravendra.proxy.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

@RestController
@RequestMapping("/proxy/forward")
public class ProxyController {
    private final WebClient webClient;
    String serverBaseUrl = "http://localhost:8081/api";

    public ProxyController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(serverBaseUrl).build(); // Server address
    }

    @GetMapping("/{path:.+}")
    public Mono<String> forwardRequest(ServerWebExchange exchange, @PathVariable String path) {
        String originalClientIp = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");

        if (originalClientIp == null || originalClientIp.isEmpty()) {
            originalClientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }

        return webClient.get()
                .uri("/" + path)  // Append path to the base URL
                .header("X-Forwarded-For", originalClientIp)  // Forward the original client IP
                .retrieve()
                .bodyToMono(String.class);
    }
}
