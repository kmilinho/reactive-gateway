package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.Route;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RouteRepository {

    private Map<String, Route> routes;

    public RouteRepository() {
        routes = new HashMap<>();

        routes.put("/test/something",
                Route.builder()
                        .gatewayPath("/test/something")
                        .endpoints(
                                List.of(
                                        Endpoint.builder()
                                            .serviceId("SAMPLE")
                                            .path("/users/1")
                                            .build(),
                                        Endpoint.builder()
                                                .serviceId("SAMPLE")
                                                .path("/todos/1")
                                                .build()
                                        )
                        )
                        .build()
        );
    }

    public Route getByPath(String path) {
        return routes.get(path);
    }

    public void addRoute(Route route){
        routes.put(route.getGatewayPath(), route);
    }
}
