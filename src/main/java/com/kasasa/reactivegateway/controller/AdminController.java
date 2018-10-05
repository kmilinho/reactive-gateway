package com.kasasa.reactivegateway.controller;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.service.Service;
import com.kasasa.reactivegateway.exceptions.NotFoundException;
import com.kasasa.reactivegateway.repository.EndpointRepository;
import com.kasasa.reactivegateway.repository.ServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;

@RestController
@RequestMapping("/admin")
@Priority(0)
@Slf4j
public class AdminController {

    private ServiceRepository serviceRepository;
    private EndpointRepository endpointRepository;

    public AdminController(ServiceRepository serviceRepository, EndpointRepository endpointRepository) {
        this.serviceRepository = serviceRepository;
        this.endpointRepository = endpointRepository;
    }

    @GetMapping("/service")
    Flux<Service> showAllServices() {
        return serviceRepository.getAllServices();
    }

    @GetMapping("/service/{id}")
    Mono<Service> getServiceById(@PathVariable("id") String serviceId) {
        return serviceRepository.getById(serviceId)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @PostMapping("/service")
    Mono<Service> createService(@RequestBody Mono<Service> inputService) {
        return serviceRepository.addService(inputService).map(
                (service) -> {
                    endpointRepository.getCreateEndpointsForService(service.getId());
                    return service;
                });
    }

    @GetMapping("/service/{id}/endpoint")
    Flux<Endpoint> showAllServiceEndpoints(@PathVariable("id") String serviceId) {
        return endpointRepository.getServiceEndpoints(serviceId);
    }

    @GetMapping("/service/{serviceId}/endpoint/{endpointId}")
    Mono<Endpoint> showServiceEndpoint(@PathVariable("serviceId") String serviceId, @PathVariable("endpointId") String endpointId) {
        return endpointRepository.getServiceEndpoint(serviceId, endpointId)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @PostMapping("/service/{id}/endpoint")
    Mono<Endpoint> createEndpoint(@RequestBody Mono<Endpoint> endpoint, @PathVariable("id") String serviceId) {
        return endpointRepository.createEndpoint(serviceId, endpoint);
    }

    @DeleteMapping("/service/{serviceId}/endpoint/{endpointId}")
    Mono<Void> deleteEndpoint(@PathVariable("serviceId") String serviceId, @PathVariable("endpointId") String endpointId) {
        return endpointRepository.delete(serviceId, endpointId);
    }

    @RequestMapping("**")
    Mono<Void> defaultRoute(){
        throw new NotFoundException();
    }
}
