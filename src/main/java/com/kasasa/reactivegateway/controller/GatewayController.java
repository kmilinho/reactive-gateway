package com.kasasa.reactivegateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;

@RestController
@Priority(1)
@Slf4j
public class GatewayController {

    private final GatewayResolver gatewayResolver;
    private final WebClient serviceClient;

    public GatewayController(GatewayResolver gatewayResolver, WebClient serviceClient) {
        this.gatewayResolver = gatewayResolver;
        this.serviceClient = serviceClient;
    }

    @RequestMapping()
    public Mono<Object> handle(ServerHttpRequest request) {

        log.info("request received: " + request.getMethod() + " " + request.getURI());

        GatewayRequest gatewayRequest = gatewayResolver.resolve(request);

        //TODO Middlewares: (gatewayRequest ->process with middleware -> gatewayRequest)

        return serviceClient.mutate()
                .baseUrl(gatewayRequest.getService().getResolveInfo().getUrl())
                .build()
                .get()
                .uri(gatewayRequest.getEndpoint().getPath())
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Object.class));
    }
}
