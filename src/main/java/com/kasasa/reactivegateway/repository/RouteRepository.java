package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.route.Route;
import com.kasasa.reactivegateway.exceptions.BadRequestException;
import com.kasasa.reactivegateway.exceptions.NotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class RouteRepository {

    private Map<String, Route> routes;

    private EndpointRepository endpointRepository;

    public RouteRepository(EndpointRepository endpointRepository) {
        routes = new HashMap<>();
        this.endpointRepository = endpointRepository;
    }

    /**
     *
     * @param path
     * @return
     */
    public Route getByPath(String path) {
        return routes.get(path);
    }

    /**
     *
     * @return
     */
    public Collection<Route> getAll() {
        return routes.values();
    }

    /**
     *
     * @param route
     * @return
     */
    public Route addRoute(Route route){
        if (!validateNewRoute(route)) {
            throw new BadRequestException();
        }

        routes.put(route.getGatewayPath(), route);

        return route;
    }

    private boolean validateNewRoute(Route route) {
        if (routes.containsKey(route.getGatewayPath())) return false;

        if (route.getServiceEndpoints().isEmpty()) return false;

        try {
            route.getServiceEndpoints().iterator()
                    .forEachRemaining(serviceEndpoint -> {
                        Endpoint endpoint = endpointRepository
                                .getServiceEndpoint(serviceEndpoint.getServiceId(), serviceEndpoint.getEndpointPath());
                        if (Objects.isNull(endpoint)) {
                            throw new NotFoundException();
                        }
                    });
        } catch (NotFoundException e) {
            return false;
        }

        return true;
    }
}
