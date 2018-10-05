package com.kasasa.reactivegateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveGatewayApplicationTests {

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @Before
    public void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void testGetAllServicesReturnsOkWithNoServices() {
        //when
        client.get().uri("admin/service").exchange()

                //then
                .expectStatus().isOk();
    }

    @Test
    public void testReturnsNotFoundForNonExistentRoutes() {
        //when
        client.get().uri("does-not-exist").exchange()

                //then
                .expectStatus().is4xxClientError();
    }
}
