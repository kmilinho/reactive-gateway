package com.kasasa.reactivegateway.middleware.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Component
public class ServiceMiddlewareEngine {

    private ServiceMiddlewareResolver serviceMiddlewareResolver;

    public ServiceMiddlewareEngine(ServiceMiddlewareResolver serviceMiddlewareResolver) {
        this.serviceMiddlewareResolver = serviceMiddlewareResolver;
    }

    public WebClient applyMiddleware(List<MiddlewareType> middlewareTypes, WebClient client) {
        if (Objects.isNull(middlewareTypes) || middlewareTypes.isEmpty()) return client;

        for (MiddlewareType middlewareType : middlewareTypes) {
            client = serviceMiddlewareResolver.resolve(middlewareType).apply(client);
        }

        return client;
    }
}
