package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.endpoint.Endpoint;
import com.kasasa.reactivegateway.exceptions.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EndpointRepository {

    private Map<String, Map<String, Endpoint>> serviceEndpoints;

    public EndpointRepository() {
        serviceEndpoints = new ConcurrentHashMap<>();
    }

    public Mono<Endpoint> getServiceEndpoint(String serviceId, String endpointId) throws NotFoundException {
        return getServiceEndpoints(serviceId)
                .filter((endpoint) -> endpoint.getId().equals(endpointId))
                .next();
    }

    public Flux<Endpoint> getServiceEndpoints(String serviceId) throws NotFoundException {
        return Flux.fromStream(() -> {
            Map<String, Endpoint> endpoints = serviceEndpoints.get(serviceId);

            if (Objects.isNull(endpoints) || endpoints.isEmpty()) {
                throw new NotFoundException();
            }

            return endpoints.values().stream();
        });
    }

    public Mono<Endpoint> createEndpoint(String serviceId, Mono<Endpoint> monoEndpoint) {
        return monoEndpoint.flatMap(endpoint -> {
            Map<String, Endpoint> endpoints;
            if (!serviceEndpoints.containsKey(serviceId)) {
                endpoints = new ConcurrentHashMap<>();
                serviceEndpoints.put(serviceId, endpoints);
            } else {
                endpoints = serviceEndpoints.get(serviceId);
            }
            String id = UUID.randomUUID().toString();
            endpoint.setId(id);
            endpoint.setServiceId(serviceId);

            return Mono.fromSupplier(() -> {
                endpoints.put(id, endpoint);
                return endpoint;
            });
        });
    }

    public Mono<Void> delete(String serviceId, String endpointId) {
        serviceEndpoints.get(serviceId).remove(endpointId);
        return Mono.empty();
    }
}
