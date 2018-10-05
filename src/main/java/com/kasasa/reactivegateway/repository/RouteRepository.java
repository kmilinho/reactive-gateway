package com.kasasa.reactivegateway.repository;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.route.Route;
import com.kasasa.reactivegateway.exceptions.BadRequestException;
import com.kasasa.reactivegateway.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

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
        validateNewRoute(route);

        routes.put(route.getGatewayPath(), route);

        return route;
    }

    private void validateNewRoute(Route route) {
        if (routes.containsKey(route.getGatewayPath()))
            throw new BadRequestException("Gateway path needs to be unique.");

        if (route.getServiceEndpoints().isEmpty())
            throw new BadRequestException("An endpoint needs to specify service endpoints to be called.");

        try {
            route.getServiceEndpoints().iterator()
                    .forEachRemaining(serviceEndpoint -> {
                        Endpoint endpoint = endpointRepository
                                .getServiceEndpoint(serviceEndpoint.getServiceId(), serviceEndpoint.getEndpointPath());
                        if (Objects.isNull(endpoint)) {
                            throw new NotFoundException(String.format(
                                    "Service '%s' and/or endpoint '%s' were not found.",
                                    serviceEndpoint.getServiceId(),
                                    serviceEndpoint.getEndpointPath()
                            ));
                        }
                    });
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getReason());
        }
    }
}
