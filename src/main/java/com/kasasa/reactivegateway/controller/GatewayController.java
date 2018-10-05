package com.kasasa.reactivegateway.controller;

import com.kasasa.reactivegateway.EndpointCaller;
import com.kasasa.reactivegateway.RouteResolver;
import com.kasasa.reactivegateway.dto.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;

@RestController
@Priority(1)
@Slf4j
public class GatewayController {

    private final RouteResolver routeResolver;
    private final EndpointCaller endpointCaller;

    public GatewayController(RouteResolver routeResolver, EndpointCaller endpointCaller) {
        this.routeResolver = routeResolver;
        this.endpointCaller = endpointCaller;
    }

    @RequestMapping()
    public Mono<String> handle(ServerHttpRequest request) {

        log.info("request received: " + request.getMethod() + " " + request.getURI());

        Route route = routeResolver.resolve(request);

        return route.getEndpoints().parallelStream().map(endpointCaller::call)
                .reduce(Mono.just(""),
                        (gatewayResponse, endpointResponse) ->
                                gatewayResponse.zipWith(endpointResponse).map(tuple -> "hello world")
                );
    }
}
