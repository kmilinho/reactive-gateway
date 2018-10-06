package com.kasasa.reactivegateway.middleware.service;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class Middleware {
    public abstract WebClient apply(WebClient client);
}
