package com.kasasa.reactivegateway.middleware;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

@Component
public class MiddlewareEngine {

    private MiddlewareResolver middlewareResolver;

    public MiddlewareEngine(MiddlewareResolver middlewareResolver) {
        this.middlewareResolver = middlewareResolver;
    }

    public ServerHttpRequest applyMiddleware(List<MiddlewareType> inputMiddleware, ServerHttpRequest request) {
        if (Objects.isNull(inputMiddleware) || inputMiddleware.isEmpty()) return request;

        try {
            for (MiddlewareType middlewareType : inputMiddleware) {
                request = middlewareResolver.resolve(middlewareType).newInstance(request).apply();
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return request;
    }
}
