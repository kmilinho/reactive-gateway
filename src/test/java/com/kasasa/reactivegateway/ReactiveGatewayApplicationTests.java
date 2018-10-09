package com.kasasa.reactivegateway;

import com.kasasa.reactivegateway.dto.Parameter;
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
public class ReactiveGatewayApplicationTests {

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
    public void testReturnsNotFoundForNonExistentRoutes() {
        //when
        client.get().uri("does-not-exist").exchange()

                //then
                .expectStatus().isNotFound();
    }

    @Test
    public void testResolvesRoute() {
        //given
        String gatewayPath = "/testResolvesRoute/path";
        String serviceId = "testResolvesRoute-service";
        String endpointPath1 = "/users/1";
        String endpointPath2 = "/todos/1";
        String expectedFinalBody = "{\"userName\":\"Leanne Graham\",\"userEmail\":\"Sincere@april.biz\",\"taskTitle\":\"delectus aut autem\",\"isTaskCompleted\":false,\"authors\":[\"Yavor\",\"Camilo\"]}";
        String authorsServiceId = "authorsService";
        String autorsGetAllPath = "/hackathon/reactive-gateway/authors";

        testApiClient.createService(serviceId, "https://jsonplaceholder.typicode.com");

        testApiClient.createService(authorsServiceId, "http://demo0124104.mockable.io");


        var parameters1 = List.of(
                Parameter.builder().key("name").mappedToKey("userName").build(),
                Parameter.builder().key("email").mappedToKey("userEmail").build()
        );

        var parameters2 = List.of(
                Parameter.builder().key("title").mappedToKey("taskTitle").build(),
                Parameter.builder().key("completed").mappedToKey("isTaskCompleted").build()
        );

        var parameters3 = List.of(
                Parameter.builder().key("authors").mappedToKey("authors").build()
        );

        testApiClient.createEndpoint(serviceId, endpointPath1);
        testApiClient.createEndpoint(serviceId, endpointPath2);
        testApiClient.createEndpoint(authorsServiceId, autorsGetAllPath);


        List<ServiceEndpoint> endpoints = List.of(
                ServiceEndpoint.builder()
                        .serviceId(serviceId)
                        .endpointPath(endpointPath1)
                        .outputParameters(parameters1)
                        .build(),
                ServiceEndpoint.builder()
                        .serviceId(serviceId)
                        .endpointPath(endpointPath2)
                        .outputParameters(parameters2)
                        .build(),
                ServiceEndpoint.builder()
                        .serviceId(authorsServiceId)
                        .endpointPath(autorsGetAllPath)
                        .outputParameters(parameters3)
                        .build()
        );

        //when
        testApiClient.createRoute(gatewayPath, endpoints)

                //then
                .expectStatus().isOk();

        //when
        client.get().uri(gatewayPath).exchange()
                //then
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(body -> {
                    Assert.assertEquals(expectedFinalBody, body.getResponseBody());
                });
    }
}
