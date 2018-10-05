package com.kasasa.reactivegateway.middleware;

import com.kasasa.reactivegateway.middleware.gateway.AuthHeaderMiddleware;
import com.kasasa.reactivegateway.middleware.gateway.LoggerMiddleware;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@Component
public class MiddlewareResolver {

    private Map<MiddlewareType, Class<? extends Middleware>> middlewareTypeMap;

    public MiddlewareResolver() {
        middlewareTypeMap = new HashMap<>();
        middlewareTypeMap.put(MiddlewareType.AUTH_HEADER, AuthHeaderMiddleware.class);
        middlewareTypeMap.put(MiddlewareType.LOGGER, LoggerMiddleware.class);
    }

    Constructor<? extends Middleware> resolve(MiddlewareType type) throws NoSuchMethodException {
        if (!middlewareTypeMap.containsKey(type)) {
            throw new RuntimeException("Unknown middleware: " + type);
        }

        return middlewareTypeMap.get(type).getConstructor(ServerHttpRequest.class);
    }
}
