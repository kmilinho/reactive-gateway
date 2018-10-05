package com.kasasa.reactivegateway;

import com.kasasa.reactivegateway.dto.route.ServiceEndpoint;
import com.kasasa.reactivegateway.helpers.ApiClient;
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
public class ReactiveGatewayApplicationTests {

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
    public void testReturnsNotFoundForNonExistentRoutes() {
        //when
        client.get().uri("does-not-exist").exchange()

                //then
                .expectStatus().is4xxClientError();
    }

    @Test
    public void testResolvesRoute() {
        //given
        String gatewayPath = "/testResolvesRoute/path";
        String serviceId = "testResolvesRoute-service";
        String endpointPath1 = "/users/1";
        String endpointPath2 = "/todos/1";
        apiClient.createService(serviceId, "https://jsonplaceholder.typicode.com");
        apiClient.createEndpoint(serviceId, endpointPath1);
        apiClient.createEndpoint(serviceId, endpointPath2);
        List<ServiceEndpoint> endpoints = List.of(
            ServiceEndpoint.builder()
                .serviceId(serviceId)
                .endpointPath(endpointPath1)
                .build(),
            ServiceEndpoint.builder()
                .serviceId(serviceId)
                .endpointPath(endpointPath2)
                .build()
        );

        //when
        apiClient.createRoute(gatewayPath, endpoints)

                //then
                .expectStatus().isOk();

        //when
        client.get().uri(gatewayPath).exchange()

                //then
                .expectStatus().isOk();

        // TODO Verify merged responses
    }
}
