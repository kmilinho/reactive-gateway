package com.kasasa.reactivegateway.handler;

import com.kasasa.reactivegateway.Service.Service;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GatewayHandler {

    public Mono<ServerResponse> handle(ServerRequest request) {

        log.info("********* REQUEST RECEIVED *********");
        log.info("path: " + request.path());
        log.info("method: " + request.methodName());
        log.info("headers: " + request.headers());
        log.info("Processing thread: " + Thread.currentThread().getId());
        log.info("Total threads: " + Thread.activeCount());

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(Service.builder().id(request.path()).build());
    }
}