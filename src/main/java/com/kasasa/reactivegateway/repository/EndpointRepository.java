package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.endpoint.Endpoint;
import com.kasasa.reactivegateway.exceptions.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class EndpointRepository {

    private Map<String, Map<String, Endpoint>> serviceEndpoints;

    public EndpointRepository() {
        serviceEndpoints = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param serviceId
     * @param endpointId
     * @return
     * @throws NotFoundException
     */
    public Mono<Endpoint> getServiceEndpoint(String serviceId, String endpointId) throws NotFoundException {
        return getServiceEndpoints(serviceId)
                .filter((endpoint) -> endpoint.getId().equals(endpointId))
                .next();
    }

    /**
     *
     * @param serviceId
     * @return
     * @throws NotFoundException
     */
    public Flux<Endpoint> getServiceEndpoints(String serviceId) throws NotFoundException {
        return Flux.fromStream(() -> {
            Map<String, Endpoint> endpoints = serviceEndpoints.get(serviceId);

            if (Objects.isNull(endpoints) || endpoints.isEmpty()) {
                throw new NotFoundException();
            }

            return endpoints.values().stream();
        });
    }

    /**
     *
     * @param serviceId
     * @param monoEndpoint
     * @return
     */
    public Mono<Endpoint> createEndpoint(String serviceId, Mono<Endpoint> monoEndpoint) {
        return monoEndpoint.map(saveEndpoint(serviceId));
    }

    /**
     *
     * @param serviceId
     * @param endpointId
     * @return
     */
    public Mono<Void> delete(String serviceId, String endpointId) {
        serviceEndpoints.get(serviceId).remove(endpointId);
        return Mono.empty();
    }

    private Function<Endpoint, Endpoint> saveEndpoint(String serviceId) {
        return endpoint -> {

            endpoint.setServiceId(serviceId);

            getEndpointsForService(serviceId)
                    .put(endpoint.getId(), endpoint);

            return endpoint;
        };
    }

    private Map<String, Endpoint> getEndpointsForService(String serviceId) {
        Map<String, Endpoint> endpoints;
        if (!serviceEndpoints.containsKey(serviceId)) {
            endpoints = new ConcurrentHashMap<>();
            serviceEndpoints.put(serviceId, endpoints);
        } else {
            endpoints = serviceEndpoints.get(serviceId);
        }
        return endpoints;
    }
}
