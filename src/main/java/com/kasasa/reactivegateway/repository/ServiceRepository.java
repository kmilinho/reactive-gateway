package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.service.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceRepository {

    private Map<String, Service> services;

    public ServiceRepository() {
        services = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param monoService
     * @return
     */
    public Mono<Service> addService(Mono<Service> monoService) {
        return monoService.map(service -> {
            services.put(service.getId(), service);
            return service;
        });
    }

    /**
     *
     * @return
     */
    public Flux<Service> getAllServices() {
        return Flux.fromStream(
                services.values().stream()
        );
    }

    /**
     *
     * @param serviceId
     * @return
     */
    public Mono<Service> getById(String serviceId) {
        return Mono.fromSupplier(
                () -> services.get(serviceId)
        );
    }
}
