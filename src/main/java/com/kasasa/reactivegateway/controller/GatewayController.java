package com.kasasa.reactivegateway.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kasasa.reactivegateway.EndpointCaller;
import com.kasasa.reactivegateway.RouteResolver;
import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.EndpointResponseTuple;
import com.kasasa.reactivegateway.dto.route.Route;
import com.kasasa.reactivegateway.middleware.gateway.GatewayMiddlewareEngine;
import com.kasasa.reactivegateway.repository.EndpointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Priority(1)
@Slf4j
public class GatewayController {

    private final RouteResolver routeResolver;
    private final EndpointCaller endpointCaller;
    private final EndpointRepository endpointRepository;
    private final JsonParser jsonParser;
    private final GatewayMiddlewareEngine gatewayMiddlewareEngine;

    public GatewayController(RouteResolver routeResolver,
                             EndpointCaller endpointCaller,
                             JsonParser jsonParser,
                             EndpointRepository endpointRepository,
                             GatewayMiddlewareEngine gatewayMiddlewareEngine
    ) {
        this.routeResolver = routeResolver;
        this.endpointCaller = endpointCaller;
        this.jsonParser = jsonParser;
        this.endpointRepository = endpointRepository;
        this.gatewayMiddlewareEngine = gatewayMiddlewareEngine;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> handle(ServerHttpRequest request) {

        Route route = routeResolver.resolve(request);

        request = gatewayMiddlewareEngine.applyMiddleware(route.getMiddleware(), request);

        List<Mono<EndpointResponseTuple>> endpointResponseMonos = route.getServiceEndpoints().parallelStream()
                .map(serviceEndpoint -> endpointRepository.getServiceEndpoint(serviceEndpoint.getServiceId(), serviceEndpoint.getEndpointPath()))
                .map(mapToEndpointResponse())
                .collect(Collectors.toList());

        return Flux.mergeSequential(endpointResponseMonos)
                .collect(JsonObject::new, (jsonGatewayResponse, endpointResponseTuple) -> {

                    var newResponse = jsonParser.parse(endpointResponseTuple.getEndpointResponse()).getAsJsonObject();

                    var wantedParameters = endpointResponseTuple.getEndpoint().getOutputParameters();
                    if (Objects.nonNull(wantedParameters)) {
                        newResponse.entrySet().forEach(entry -> {

                            var k = entry.getKey();
                            var v = entry.getValue();

                            var wanted = wantedParameters.stream().filter(parameter -> parameter.getKey().equals(k)).findFirst();

                            wanted.ifPresent(parameter -> jsonGatewayResponse.add(parameter.getMappedToKey(), v));
                        });
                    }
                })
                .map(JsonElement::toString);
    }

    private Function<Endpoint, Mono<EndpointResponseTuple>> mapToEndpointResponse() {
        return endpoint -> endpointCaller.call(endpoint)
                .map(monoResponse -> EndpointResponseTuple.builder()
                        .endpoint(endpoint)
                        .endpointResponse(monoResponse)
                        .build()
                );
    }
}
