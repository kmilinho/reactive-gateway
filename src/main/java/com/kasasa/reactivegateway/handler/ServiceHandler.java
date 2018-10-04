package com.kasasa.reactivegateway.handler;

import com.kasasa.reactivegateway.Service.Service;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class ServiceHandler {
    public static Mono<ServerResponse> store(ServerRequest request) {
        Mono<Service> monoService = request.bodyToMono(Service.class);

        return monoService.flatMap(service -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromObject(service)));
    }
}
