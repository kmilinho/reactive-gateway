package com.kasasa.reactivegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveGatewayApplication.class, args);

		GreetingWebClient gwc = new GreetingWebClient();
		System.out.println(gwc.getResult());

	}
}
