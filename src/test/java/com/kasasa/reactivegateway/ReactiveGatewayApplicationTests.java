package com.kasasa.reactivegateway;

import com.kasasa.reactivegateway.dto.service.ResolveInfo;
import com.kasasa.reactivegateway.dto.service.ResolveMethod;
import com.kasasa.reactivegateway.dto.service.Service;
import com.kasasa.reactivegateway.middleware.MiddlewareType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

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
    public void testGetAllServices() {
        //given
        createServices();

        //when
        client.get().uri("admin/service").exchange()

                //then
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Service>>() {
                }).consumeWith(result -> {
                    result.getResponseBody().stream()
                            .filter(service -> service.getId().equals("jsonplaceholder"))
                            .forEach(s -> Assert.assertEquals("https://jsonplaceholder.typicode.com", s.getResolveInfo().getUrl()));

                    result.getResponseBody().stream()
                            .filter(service -> service.getId().equals("google"))
                            .forEach(s -> Assert.assertEquals("https://google.com", s.getResolveInfo().getUrl()));

                    result.getResponseBody().stream()
                            .filter(service -> service.getId().equals("something-else"))
                            .forEach(s -> Assert.assertEquals("https://something-else.com", s.getResolveInfo().getUrl()));
                }
        );
    }

    @Test
    public void testGetServiceById() {
        //given
        createServices();

        //when
        client.get().uri("admin/service/jsonplaceholder").exchange()

                //then
                .expectStatus().isOk()
                .expectBody(Service.class).consumeWith(
                result ->
                        Assert.assertEquals("https://jsonplaceholder.typicode.com", result.getResponseBody().getResolveInfo().getUrl())
        );
    }


    @Test
    public void testItCanCreateService() {
        //given
        Service receivedService = Service.builder()
                .id("jsonplaceholder")
                .inputMiddleware(Arrays.asList(MiddlewareType.AUTH_HEADER))
                .resolveMethod(ResolveMethod.URL)
                .resolveInfo(
                        ResolveInfo.builder()
                                .url("https://jsonplaceholder.typicode.com")
                                .build()
                )
                .build();

        //when
        client.post().uri("/admin/service").syncBody(receivedService).exchange()

                //then
                .expectStatus().isOk()
                .expectBody(Service.class).consumeWith(
                result -> {
                    Service serviceBody = result.getResponseBody();
                    Assert.assertEquals("jsonplaceholder", serviceBody.getId());
                    Assert.assertEquals(receivedService.getInputMiddleware(), serviceBody.getInputMiddleware());
                    Assert.assertNull(receivedService.getOutputMiddleware());
                    Assert.assertEquals(receivedService.getResolveInfo(), serviceBody.getResolveInfo());
                }
        );
    }

    private void createServices() {
        Service s1 = Service.builder()
                .id("jsonplaceholder")
                .inputMiddleware(Arrays.asList(MiddlewareType.AUTH_HEADER))
                .resolveMethod(ResolveMethod.URL)
                .resolveInfo(
                        ResolveInfo.builder()
                                .url("https://jsonplaceholder.typicode.com")
                                .build()
                )
                .build();
        Service s2 = Service.builder()
                .id("google")
                .inputMiddleware(Arrays.asList(MiddlewareType.AUTH_HEADER))
                .resolveMethod(ResolveMethod.URL)
                .resolveInfo(
                        ResolveInfo.builder()
                                .url("https://google.com")
                                .build()
                )
                .build();
        Service s3 = Service.builder()
                .id("something-else")
                .inputMiddleware(Arrays.asList(MiddlewareType.AUTH_HEADER))
                .resolveMethod(ResolveMethod.URL)
                .resolveInfo(
                        ResolveInfo.builder()
                                .url("https://something-else.com")
                                .build()
                )
                .build();
        client.post().uri("/admin/service").syncBody(s1).exchange();
        client.post().uri("/admin/service").syncBody(s2).exchange();
        client.post().uri("/admin/service").syncBody(s3).exchange();

    }
}
