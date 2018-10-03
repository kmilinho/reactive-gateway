package com.kasasa.reactivegateway.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class GatewayHandler {

    public Mono<ServerResponse> handle(ServerRequest request) {
        return ServerResponse.ok()
                .body(BodyInserters.fromObject("Path Requested: " + request.path()));
    }
}