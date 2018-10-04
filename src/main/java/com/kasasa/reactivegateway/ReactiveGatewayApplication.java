package com.kasasa.reactivegateway;

import com.kasasa.reactivegateway.repository.EndpointRepository;
import com.kasasa.reactivegateway.repository.ServiceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReactiveGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveGatewayApplication.class, args);
    }

    @Bean
    public ServiceRepository getServiceRepository() {
        return new ServiceRepository();
    }


    @Bean
    public EndpointRepository getEndpointRepository() {
        return new EndpointRepository();
    }
}
