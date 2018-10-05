package com.kasasa.reactivegateway.controller;

import com.kasasa.reactivegateway.dto.route.Route;
import com.kasasa.reactivegateway.dto.route.ServiceEndpoint;
import com.kasasa.reactivegateway.helpers.TestApiClient;
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

    private TestApiClient testApiClient;

    @Before
    public void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
        testApiClient = new TestApiClient(client);
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
        testApiClient.createRoute(gatewayPath, endpoints)

                // then
                .expectStatus().isBadRequest()
                .expectBody(Exception.class)
                .consumeWith(result -> {
                    Assert.assertEquals("Service not found for 'non-existent'.", result.getResponseBody().getMessage());
                });
    }

    @Test
    public void testCreateRoute() {
        //given
        String gatewayPath = "/testCreateRoute/path";
        String serviceId = "testCreateRoute";
        String endpointPath = "/other/testCreateRoute/path";
        testApiClient.createService(serviceId, "https://jsonplaceholder.typicode.com");
        testApiClient.createEndpoint(serviceId, endpointPath);

        List<ServiceEndpoint> endpoints = List.of(ServiceEndpoint.builder()
                .serviceId(serviceId)
                .endpointPath(endpointPath)
                .build()
        );

        //when
        testApiClient.createRoute(gatewayPath, endpoints)

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
        testApiClient.createService(serviceId, "https://jsonplaceholder.typicode.com");
        testApiClient.createEndpoint(serviceId, endpointPath);
        List<ServiceEndpoint> endpoints = List.of(ServiceEndpoint.builder().serviceId(serviceId).endpointPath(endpointPath).build());
        testApiClient.createRoute(gatewayPath, endpoints);

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
