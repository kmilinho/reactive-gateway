package com.kasasa.reactivegateway.middleware;

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
public class LoggerTest {

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
    public void testLoggerMiddlewareWorks() {
        //given
        String gatewayPath = "/testLoggerMiddlewareWorks/path";
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
        apiClient.createRoute(gatewayPath, endpoints, List.of(MiddlewareType.LOGGER))

                //then
                .expectStatus().isOk();

        //when
        client.get().uri(gatewayPath).exchange()

                //then
                .expectStatus().isOk();
    }
}
