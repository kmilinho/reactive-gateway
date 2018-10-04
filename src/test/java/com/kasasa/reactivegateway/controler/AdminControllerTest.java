package com.kasasa.reactivegateway.controler;

import com.kasasa.reactivegateway.dto.endpoint.Endpoint;
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
public class AdminControllerTest {

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @Before
    public void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void testFailsWhenGivenInvalidServiceId() {
        client.get().uri("/admin/service/123/endpoint")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void testGetsEndpoints() {
        createEndpoints();

        client.get().uri("/admin/service/abc/endpoint")
                .exchange()
                .expectStatus().isOk();
    }

    private void createEndpoints() {
        Endpoint e1 = Endpoint.builder()
                .id("some-id-1")
                .build();
        Endpoint e2 = Endpoint.builder()
                .id("some-id-2")
                .build();
        Endpoint e3 = Endpoint.builder()
                .id("some-id-3")
                .build();
        client.post().uri("/admin/service/abc/endpoint").syncBody(e1).exchange();
        client.post().uri("/admin/service/abc/endpoint").syncBody(e2).exchange();
        client.post().uri("/admin/service/abc/endpoint").syncBody(e3).exchange();
    }

}
