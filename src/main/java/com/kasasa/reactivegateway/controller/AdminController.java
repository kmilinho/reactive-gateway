package com.kasasa.reactivegateway.controller;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.route.Route;
import com.kasasa.reactivegateway.dto.service.Service;
import com.kasasa.reactivegateway.exceptions.NotFoundException;
import com.kasasa.reactivegateway.repository.EndpointRepository;
import com.kasasa.reactivegateway.repository.RouteRepository;
import com.kasasa.reactivegateway.repository.ServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Priority;
import java.util.Collection;

@RestController
@RequestMapping("/admin")
@Priority(0)
@Slf4j
public class AdminController {

    private ServiceRepository serviceRepository;
    private EndpointRepository endpointRepository;
    private RouteRepository routeRepository;

    public AdminController(ServiceRepository serviceRepository, EndpointRepository endpointRepository, RouteRepository routeRepository) {
        this.serviceRepository = serviceRepository;
        this.endpointRepository = endpointRepository;
        this.routeRepository = routeRepository;
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

    @GetMapping("/service/{serviceId}/endpoint/{endpointPath}")
    Mono<Endpoint> showServiceEndpoint(@PathVariable("serviceId") String serviceId, @PathVariable("endpointPath") String endpointPath) {
        return endpointRepository.getServiceEndpoint(serviceId, endpointPath)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @PostMapping("/service/{id}/endpoint")
    Mono<Endpoint> createEndpoint(@RequestBody Mono<Endpoint> endpoint, @PathVariable("id") String serviceId) {
        return endpointRepository.createEndpoint(serviceId, endpoint);
    }

    @DeleteMapping("/service/{serviceId}/endpoint/{endpointPath}")
    Mono<Void> deleteEndpoint(@PathVariable("serviceId") String serviceId, @PathVariable("endpointPath") String endpointPath) {
        return endpointRepository.delete(serviceId, endpointPath);
    }

    @GetMapping("/route")
    Collection<Route> showAllRoutes() {
        return routeRepository.getAll();
    }

    @PostMapping("/route")
    Route createRoute(@RequestBody Route route) {
        return routeRepository.addRoute(route);
    }

    @RequestMapping("**")
    Mono<Void> defaultRoute(){
        throw new NotFoundException();
    }
}
