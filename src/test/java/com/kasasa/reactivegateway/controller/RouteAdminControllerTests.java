package com.kasasa.reactivegateway.controller;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.route.Route;
import com.kasasa.reactivegateway.dto.route.ServiceEndpoint;
import com.kasasa.reactivegateway.helpers.ApiClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteAdminControllerTests {

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    private ApiClient apiClient;

    @Before
    public void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
        apiClient = new ApiClient(client);
    }

    @Test
    public void testGetAllRoutesReturnsOkWithNoRoutes() {
        //when
        client.get().uri("admin/route").exchange()

                //then
                .expectStatus().isOk();
    }

    @Test
    public void testFailToCreateRouteWithInvalidEndpoint() {
        //given
        String gatewayPath = "/testFailToCreateRouteWithInvalidEndpoint/path";
        String serviceId = "non-existent";
        String endpointPath = "/non-existent/path";

        List<ServiceEndpoint> endpoints = List.of(ServiceEndpoint.builder()
                .serviceId(serviceId)
                .endpointPath(endpointPath)
                .build()
        );

        //when
        apiClient.createRoute(gatewayPath, endpoints)

                // then
                .expectStatus().isBadRequest();
    }

    @Test
    public void testCreateRoute() {
        //given
        String gatewayPath = "/testCreateRoute/path";
        String serviceId = "testCreateRoute";
        String endpointPath = "/other/testCreateRoute/path";
        apiClient.createService(serviceId, "https://jsonplaceholder.typicode.com");
        apiClient.createEndpoint(serviceId, endpointPath);

        List<ServiceEndpoint> endpoints = List.of(ServiceEndpoint.builder()
                .serviceId(serviceId)
                .endpointPath(endpointPath)
                .build()
        );

        //when
        apiClient.createRoute(gatewayPath, endpoints)

                // then
                .expectStatus().isOk()
                .expectBody(Route.class)
                .consumeWith((result) -> {
                    Route route = result.getResponseBody();
                    Assert.assertEquals(gatewayPath, route.getGatewayPath());
                    Assert.assertEquals(serviceId, route.getServiceEndpoints().get(0).getServiceId());
                    Assert.assertEquals(endpointPath, route.getServiceEndpoints().get(0).getEndpointPath());
                });
    }

    @Test
    public void testGetAllRoutes() {
        //given
        String gatewayPath = "/testGetAllRoutes/path";
        String serviceId = "testGetAllRoutes";
        String endpointPath = "/other/testGetAllRoutes/path";
        apiClient.createService(serviceId, "https://jsonplaceholder.typicode.com");
        apiClient.createEndpoint(serviceId, endpointPath);
        List<ServiceEndpoint> endpoints = List.of(ServiceEndpoint.builder().serviceId(serviceId).endpointPath(endpointPath).build());
        apiClient.createRoute(gatewayPath, endpoints);

        //when
        client.get().uri("admin/route").exchange()

                //then
                .expectStatus().isOk()
                .expectBodyList(Route.class)
                .consumeWith((result) -> {
                    Route route;
                    int i = 0;
                    do {
                        route = result.getResponseBody().get(i++);
                    }
                    while (!route.getGatewayPath().equals(gatewayPath) && i < result.getResponseBody().size());
                    Assert.assertEquals(gatewayPath, route.getGatewayPath());
                    Assert.assertEquals(serviceId, route.getServiceEndpoints().get(0).getServiceId());
                    Assert.assertEquals(endpointPath, route.getServiceEndpoints().get(0).getEndpointPath());
                });
    }
}
