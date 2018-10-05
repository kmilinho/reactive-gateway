package com.kasasa.reactivegateway.controller;

import com.kasasa.reactivegateway.dto.endpoint.Endpoint;
import com.kasasa.reactivegateway.dto.service.ResolveInfo;
import com.kasasa.reactivegateway.dto.service.Service;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class GatewayResolver {

    public GatewayRequest resolve(ServerHttpRequest request) {

        //TODO Implement endpoint and service lookup

        Service service = Service.builder()
                .resolveInfo(
                        ResolveInfo.builder()
                                .url("https://jsonplaceholder.typicode.com")
                                .build()
                )
                .build();

        Endpoint endpoint = Endpoint.builder()
                .path("/users")
                .build();

        return GatewayRequest.builder()
                .service(service)
                .endpoint(endpoint)
                .build();
    }
}
