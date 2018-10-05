package com.kasasa.reactivegateway.middleware.gateway;

import com.kasasa.reactivegateway.middleware.Middleware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Slf4j
public class LoggerMiddleware extends Middleware {

    public LoggerMiddleware(ServerHttpRequest request) {
        super(request);
    }

    public ServerHttpRequest apply() {
        log.info("* * * * * * Request received: " + request.getMethod() + " " + request.getURI());

        return request;
    }
}
