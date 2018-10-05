package com.kasasa.reactivegateway.dto.route;

import com.kasasa.reactivegateway.middleware.MiddlewareType;
import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * This represent the routes that the gateway accepts from the outside world.
 * Reactive Gateway will resolve request to this route based on the configuration
 * of this object, created when the route was added.
 */
public class Route {

    private String gatewayPath;

    private List<MiddlewareType> inputMiddleware;

    private List<ServiceEndpoint> serviceEndpoints;
}
