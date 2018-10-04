package com.kasasa.reactivegateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
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
    public void testRespondsWithErrorForUnknownRoute() {
        client.get().uri("/persons/1")
                .exchange()
                .expectStatus().isOk()
        .expectBody().json("{\"id\":\"/persons/1\"}");
    }

	@Test
	public void respondsToAdminServiceRequest() {
        client.post().uri("/admin/service")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody("{\"id\":\"service1\", \"resolveMethod\":\"url\",\"resolveInfo\":{\"url\":\"http://www.service.com\"},\"inputMiddleware\":[\"auth_header\"]}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{\n" +
                        "    \"id\": \"service1\",\n" +
                        "    \"resolveMethod\": \"url\",\n" +
                        "    \"resolveInfo\": {\n" +
                        "        \"url\": \"http://www.service.com\",\n" +
                        "        \"dns\": null\n" +
                        "    },\n" +
                        "    \"inputMiddleware\": [\n" +
                        "        \"auth_header\"\n" +
                        "    ],\n" +
                        "    \"outputMiddleware\": null\n" +
                        "}");
	}

}
