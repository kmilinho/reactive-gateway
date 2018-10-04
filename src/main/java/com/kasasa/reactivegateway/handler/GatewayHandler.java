package com.kasasa.reactivegateway.handler;

import com.kasasa.reactivegateway.Service.Service;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class GatewayHandler {

    public Mono<ServerResponse> handle(ServerRequest request) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(Service.builder().id(request.path()).build());
    }
}