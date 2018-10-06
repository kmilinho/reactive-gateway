package com.kasasa.reactivegateway.middleware.gateway;

import com.kasasa.reactivegateway.exceptions.UnauthorizedException;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class AuthHeaderMiddleware extends Middleware {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final static String AUTHORIZATION_KEY = "Basic dGVzdDpwYXNz";

    public ServerHttpRequest apply(ServerHttpRequest request) {
        var headers = request.getHeaders();
        if (!headers.containsKey(AUTHORIZATION_HEADER) || !headers.get(AUTHORIZATION_HEADER).contains(AUTHORIZATION_KEY)) {
            throw new UnauthorizedException();
        }

        return request;
    }
}
