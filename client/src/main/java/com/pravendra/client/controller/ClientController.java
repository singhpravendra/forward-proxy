package com.pravendra.client.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    private final WebClient webClient;

    final String proxyBaseUrl = "http://localhost:8082/proxy/forward";

    public ClientController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(proxyBaseUrl).build(); // Proxy address
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientController.class, args);
    }

    @GetMapping("/hello")
    public Mono<String> callHello(ServerWebExchange exchange) {
        String originalClientIp = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        return webClient.get()
                .uri("/hello")
                .retrieve()
                .bodyToMono(String.class);
    }
}
