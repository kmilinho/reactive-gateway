package com.kasasa.reactivegateway.controller;

import com.kasasa.reactivegateway.dto.Endpoint;
import com.kasasa.reactivegateway.dto.service.Service;
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
public class ServiceEndpointAdminControllerTest {

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
    public void testFailsWhenServiceIdNotFound() {
        client.get().uri("/admin/service/123/endpoint")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testFailsWhenEndpointPathNotFound() {
        // when
        apiClient.createEndpoint("s2", "some-id-1");

        // then
        client.get().uri("/admin/service/s2/endpoint/some-other-id")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testReturnsEmptyWhenServiceFoundButHasNoEndpoints() {
        // given
        Service service = Service.builder().id("some-service-id").build();
        client.post().uri("/admin/service").syncBody(service).exchange();

        // when
        client.get().uri("/admin/service/some-service-id/endpoint").exchange()

                // then
                .expectStatus().isOk();
    }

    @Test
    public void testCreateEndpoint() {
        // when
        apiClient.createEndpoint("s1", "some-id-1")

                // then
                .expectStatus().isOk()
                .expectBody(Endpoint.class)
                .consumeWith((result) -> {
                    Endpoint ep = result.getResponseBody();
                    Assert.assertEquals("some-id-1", ep.getPath());
                    Assert.assertEquals("s1", ep.getServiceId());
                });
    }

    @Test
    public void testGetsEndpoint() {
        // given
        apiClient.createEndpoint("abc", "some-id-1");

        // when
        client.get().uri("/admin/service/abc/endpoint/some-id-1").exchange()

                // then
                .expectStatus().isOk()
                .expectBody(Endpoint.class)
                .consumeWith((result) -> {
                    Endpoint ep = result.getResponseBody();
                    Assert.assertEquals("some-id-1", ep.getPath());
                    Assert.assertEquals("abc", ep.getServiceId());
                });
    }

    @Test
    public void testGetsEndpoints() {
        // given
        apiClient.createEndpoint("abc", "some-id-1");
        apiClient.createEndpoint("abc", "some-id-2");

        // when
        client.get().uri("/admin/service/abc/endpoint").exchange()

                // then
                .expectStatus().isOk()
                .expectBodyList(Endpoint.class)
                .hasSize(2)
                .consumeWith((result) -> {
                    List<Endpoint> endpointList = result.getResponseBody();
                    Assert.assertEquals("some-id-1", endpointList.get(0).getPath());
                    Assert.assertEquals("abc", endpointList.get(0).getServiceId());
                    Assert.assertEquals("some-id-2", endpointList.get(1).getPath());
                    Assert.assertEquals("abc", endpointList.get(1).getServiceId());
                });
    }

    @Test
    public void testDeletesEndpoint() {
        // given
        apiClient.createEndpoint("for-delete", "deleted-id-1");
        client.get().uri("/admin/service/for-delete/endpoint/deleted-id-1").exchange().expectStatus().isOk();

        // when
        client.delete().uri("/admin/service/for-delete/endpoint/deleted-id-1").exchange()

                // then
                .expectStatus().isOk();

        // when
        client.get().uri("/admin/service/for-delete/endpoint/deleted-id-1").exchange()

                // then
                .expectStatus().isNotFound();
    }
}
