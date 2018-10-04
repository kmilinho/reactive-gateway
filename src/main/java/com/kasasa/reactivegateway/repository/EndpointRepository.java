package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.endpoint.Endpoint;
import reactor.core.publisher.Mono;

public class EndpointRepository {
    public Mono<Endpoint> createEndpoint(String serviceId, Mono<Endpoint> monoEndpoint) {
        return monoEndpoint.flatMap(endpoint -> Mono.just(
                Endpoint.builder()
                        .id(endpoint.getId())
                        .serviceId(serviceId)
                        .build()
                )
        );
    }

    public Mono<Void> delete(String serviceId, String endpointId) {
        return Mono.empty();
    }
}
