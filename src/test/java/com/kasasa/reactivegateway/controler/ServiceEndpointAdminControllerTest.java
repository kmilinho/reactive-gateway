package com.kasasa.reactivegateway.controler;

import com.kasasa.reactivegateway.dto.endpoint.Endpoint;
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
public class ServiceEndpointAdminControllerTest {

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
    public void testGetsEndpointForValidServiceId() {
        // given
        createEndpoint("some-id-1");

        // when
        client.get().uri("/admin/service/abc/endpoint").exchange()

                // then
                .expectStatus().isOk()
                .expectBodyList(Endpoint.class)
                .hasSize(1)
                .consumeWith((result) -> {
                    List<Endpoint> endpointList = result.getResponseBody();
                    Assert.assertEquals("some-id-1", endpointList.get(0).getId());
                    Assert.assertEquals("abc", endpointList.get(0).getServiceId());
                });

        // when
        client.get().uri("/admin/service/abc/endpoint/some-id-1").exchange()

                // then
                .expectStatus().isOk()
                .expectBody(Endpoint.class)
                .consumeWith((result) -> {
                    Endpoint ep = result.getResponseBody();
                    Assert.assertEquals("some-id-1", ep.getId());
                    Assert.assertEquals("abc", ep.getServiceId());
                });
    }

    @Test
    public void testDeletesEndpoint() {
        // given
        createEndpoint("some-id-1");

        // when
        client.delete().uri("/admin/service/abc/endpoint/some-id-1").exchange()

                // then
                .expectStatus().isOk();

        // when
        client.get().uri("/admin/service/123/endpoint/some-id-1").exchange()

                // then
                .expectStatus().is4xxClientError();
    }

    private void createEndpoint(String id) {
        Endpoint endpoint = Endpoint.builder()
                .id(id)
                .build();
        client.post().uri("/admin/service/abc/endpoint").syncBody(endpoint).exchange();
    }

}
