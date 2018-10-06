package com.kasasa.reactivegateway.middleware.gateway;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayMiddlewareResolver {

    private Map<MiddlewareType, Class<? extends Middleware>> middlewareTypeMap;

    public GatewayMiddlewareResolver() {
        middlewareTypeMap = new HashMap<>();
        middlewareTypeMap.put(MiddlewareType.AUTH_HEADER, AuthHeaderMiddleware.class);
        middlewareTypeMap.put(MiddlewareType.LOGGER, LoggerMiddleware.class);
    }

    Middleware resolve(MiddlewareType type) {
        if (!middlewareTypeMap.containsKey(type)) {
            throw new RuntimeException("Unknown middleware: " + type);
        }

        try {
            return middlewareTypeMap.get(type).getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
