package com.kasasa.reactivegateway.middleware.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Slf4j
public class LoggerMiddleware extends Middleware {

    public ServerHttpRequest apply(ServerHttpRequest request) {
        log.info("* * * * * * Request received: " + request.getMethod() + " " + request.getURI());

        return request;
    }
}
