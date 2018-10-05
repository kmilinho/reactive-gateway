package com.kasasa.reactivegateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;

@RestController
@Priority(1)
@Slf4j
public class GatewayController {

    @RequestMapping()
    public Mono<Object> handle(ServerHttpRequest request) {
        log.info("request received: " + request.getMethod() + " " + request.getURI());
        return Mono.just("OK");
    }
}
