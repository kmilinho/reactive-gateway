package com.kasasa.reactivegateway.middleware.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class GatewayMiddlewareEngine {

    private GatewayMiddlewareResolver gatewayMiddlewareResolver;

    public GatewayMiddlewareEngine(GatewayMiddlewareResolver gatewayMiddlewareResolver) {
        this.gatewayMiddlewareResolver = gatewayMiddlewareResolver;
    }

    public ServerHttpRequest applyMiddleware(List<MiddlewareType> inputMiddleware, ServerHttpRequest request) {
        if (Objects.isNull(inputMiddleware) || inputMiddleware.isEmpty()) return request;

        for (MiddlewareType middlewareType : inputMiddleware) {
            request = gatewayMiddlewareResolver.resolve(middlewareType).apply(request);
        }

        return request;
    }
}
