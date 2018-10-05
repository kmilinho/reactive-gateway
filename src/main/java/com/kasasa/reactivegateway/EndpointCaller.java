package com.kasasa.reactivegateway;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.service.Service;
import com.kasasa.reactivegateway.exceptions.NotFoundException;
import com.kasasa.reactivegateway.repository.ServiceRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class EndpointCaller {

    private WebClient serviceClient;
    private ServiceRepository serviceRepository;

    public EndpointCaller(WebClient serviceClient, ServiceRepository serviceRepository) {

        this.serviceClient = serviceClient;
        this.serviceRepository = serviceRepository;
    }

    public Mono<String> call(Endpoint endpoint) {

        Service service = serviceRepository.getById(endpoint.getServiceId());

        if (Objects.isNull(service)) {
            throw new NotFoundException();
        }
        return serviceClient.mutate()
                .baseUrl(service.getResolveInfo().getUrl())
                .build()
                .get()
                .uri(endpoint.getPath())
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class));
    }
}
