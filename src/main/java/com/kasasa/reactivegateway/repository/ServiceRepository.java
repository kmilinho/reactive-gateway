package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.service.Service;
import reactor.core.publisher.Mono;

public class ServiceRepository {
    public Mono<Service> addService(Mono<Service> service) {
        return service;
    }
}
