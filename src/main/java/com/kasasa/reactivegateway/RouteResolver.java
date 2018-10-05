package com.kasasa.reactivegateway;

import com.kasasa.reactivegateway.dto.Route;
import com.kasasa.reactivegateway.exceptions.NotFoundException;
import com.kasasa.reactivegateway.repository.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j

public class RouteResolver {

    private RouteRepository routeRepository;

    public RouteResolver(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route resolve(ServerHttpRequest request) {

        String gatewayPath = request.getURI().getPath();

        log.info("gatewayPath from request=" + gatewayPath);

        Route route = routeRepository.getByPath(gatewayPath);

        if (Objects.isNull(route)) {
            throw new NotFoundException();
        }

        return route;
    }
}
