package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.service.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceRepository {

    private Map<String, Service> services;

    public ServiceRepository() {
        services = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param service
     * @return
     */
    public Service addService(Service service) {
        services.put(service.getId(), service);
        return service;
    }

    /**
     *
     * @return
     */
    public Collection<Service> getAllServices() {
        return services.values();
    }

    /**
     *
     * @param serviceId
     * @return
     */
    public Service getById(String serviceId) {
        return services.get(serviceId);
    }
}
