package com.kasasa.reactivegateway.middleware;

import com.kasasa.reactivegateway.dto.route.ServiceEndpoint;
import com.kasasa.reactivegateway.helpers.TestApiClient;
import com.kasasa.reactivegateway.middleware.gateway.MiddlewareType;
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
public class AuthHeaderTest {

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    private TestApiClient apiClient;

    @Before
    public void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
        apiClient = new TestApiClient(client);
    }

    @Test
    public void testRouteRequestIsUnauthorized() {
        //given
        String gatewayPath = "/testRouteRequestIsUnauthorized/path";
        String serviceId = "testResolvesRoute-service";
        String endpointPath1 = "/users/1";
        apiClient.createService(serviceId, "https://jsonplaceholder.typicode.com");
        apiClient.createEndpoint(serviceId, endpointPath1);
        List<ServiceEndpoint> endpoints = List.of(
                ServiceEndpoint.builder()
                        .serviceId(serviceId)
                        .endpointPath(endpointPath1)
                        .build()
        );

        //when
        apiClient.createRoute(gatewayPath, endpoints, List.of(MiddlewareType.AUTH_HEADER))

                //then
                .expectStatus().isOk();

        //when
        client.get().uri(gatewayPath).exchange()

                //then
                .expectStatus().isUnauthorized();
    }

    @Test
    public void testRouteRequestIsAuthorized() {
        //given
        String gatewayPath = "/testRouteRequestIsAuthorized/path";
        String serviceId = "testResolvesRoute-service";
        String endpointPath1 = "/users/1";
        apiClient.createService(serviceId, "https://jsonplaceholder.typicode.com");
        apiClient.createEndpoint(serviceId, endpointPath1);
        List<ServiceEndpoint> endpoints = List.of(
                ServiceEndpoint.builder()
                        .serviceId(serviceId)
                        .endpointPath(endpointPath1)
                        .build()
        );

        //when
        apiClient.createRoute(gatewayPath, endpoints, List.of(MiddlewareType.AUTH_HEADER))

                //then
                .expectStatus().isOk();

        //when
        client.mutate()
                .defaultHeader("Authorization", "Basic dGVzdDpwYXNz")
                .build()
                .get()
                .uri(gatewayPath)
                .exchange()

                //then
                .expectStatus().isOk();
    }
}
