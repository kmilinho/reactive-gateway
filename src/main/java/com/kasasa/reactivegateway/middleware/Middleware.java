package com.kasasa.reactivegateway.middleware;

import org.springframework.http.server.reactive.ServerHttpRequest;

public abstract class Middleware {
    protected ServerHttpRequest request;

    public Middleware(ServerHttpRequest request) {
        this.request = request;
    }

    public abstract ServerHttpRequest apply();
}
