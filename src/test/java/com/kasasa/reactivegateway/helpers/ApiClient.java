package com.kasasa.reactivegateway.helpers;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.route.Route;
import com.kasasa.reactivegateway.dto.route.ServiceEndpoint;
import com.kasasa.reactivegateway.dto.service.ResolveInfo;
import com.kasasa.reactivegateway.dto.service.Service;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

public class ApiClient {
    private WebTestClient client;

    public ApiClient(WebTestClient client) {
        this.client = client;
    }

    public WebTestClient.ResponseSpec createRoute(String gatewayPath, List<ServiceEndpoint> serviceEndpoints) {
        Route route = Route.builder()
                .gatewayPath(gatewayPath)
                .serviceEndpoints(serviceEndpoints)
                .build();
        return client.post().uri("/admin/route").syncBody(route).exchange();
    }

    public WebTestClient.ResponseSpec createService(String serviceId, String url) {
        Service service = Service.builder()
                .id(serviceId)
                .resolveInfo(ResolveInfo.builder()
                        .url(url)
                        .build())
                .build();
        return client.post().uri("/admin/service").syncBody(service).exchange();
    }

    public WebTestClient.ResponseSpec createEndpoint(String serviceId, String endpointPath) {
        Endpoint endpoint = Endpoint.builder()
                .serviceId(serviceId)
                .path(endpointPath)
                .build();
        return client.post().uri(String.format("/admin/service/%s/endpoint", serviceId)).syncBody(endpoint).exchange();
    }
}
