package com.kasasa.reactivegateway.middleware.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;

public abstract class Middleware {
    public abstract ServerHttpRequest apply(ServerHttpRequest request);
}
