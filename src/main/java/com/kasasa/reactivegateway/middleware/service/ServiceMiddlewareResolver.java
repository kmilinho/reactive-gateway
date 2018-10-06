package com.kasasa.reactivegateway.middleware.service;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceMiddlewareResolver {

    private Map<MiddlewareType, Class<? extends Middleware>> middlewareTypeMap;

    public ServiceMiddlewareResolver() {
        middlewareTypeMap = new HashMap<>();
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
