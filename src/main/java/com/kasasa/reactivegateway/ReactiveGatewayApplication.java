package com.kasasa.reactivegateway;

import com.kasasa.reactivegateway.handler.GatewayHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
public class ReactiveGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveGatewayApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> route(GatewayHandler gatewayHandler) {

        return RouterFunctions
                .route(RequestPredicates.all(), gatewayHandler::handle);
    }
}
