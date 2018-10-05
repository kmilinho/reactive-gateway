package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.exceptions.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class EndpointRepository {

    private Map<String, Map<String, Endpoint>> serviceEndpoints;

    public EndpointRepository() {
        serviceEndpoints = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param serviceId
     * @param endpointPath
     * @return
     * @throws NotFoundException
     */
    public Endpoint getServiceEndpoint(String serviceId, String endpointPath) throws NotFoundException {
        return getEndpointsForServiceOrFail(serviceId).get(endpointPath);
    }

    /**
     *
     * @param serviceId
     * @return
     * @throws NotFoundException
     */
    public Collection<Endpoint> getServiceEndpoints(String serviceId) throws NotFoundException {
        return getEndpointsForServiceOrFail(serviceId).values();
    }

    /**
     *
     * @param serviceId
     * @param endpoint
     * @return
     */
    public Endpoint createEndpoint(String serviceId, Endpoint endpoint) {
        endpoint.setServiceId(serviceId);
        getCreateEndpointsForService(serviceId).put(endpoint.getPath(), endpoint);

        return endpoint;
    }

    /**
     *
     * @param serviceId
     * @param endpointPath
     * @return
     */
    public void delete(String serviceId, String endpointPath) {
        getEndpointsForServiceOrFail(serviceId).remove(endpointPath);
    }

    /**
     *
     * @param serviceId
     * @return
     */
    public Map<String, Endpoint> getCreateEndpointsForService(String serviceId) {
        Map<String, Endpoint> endpoints;
        if (!serviceEndpoints.containsKey(serviceId)) {
            endpoints = new ConcurrentHashMap<>();
            serviceEndpoints.put(serviceId, endpoints);
        } else {
            endpoints = serviceEndpoints.get(serviceId);
        }

        return endpoints;
    }

    private Map<String, Endpoint> getEndpointsForServiceOrFail(String serviceId) throws NotFoundException {
        Map<String, Endpoint> endpoints = serviceEndpoints.get(serviceId);
        if (Objects.isNull(endpoints)) throw new NotFoundException(String.format("Service not found for '%s'.", serviceId));

        return endpoints;
    }
}
