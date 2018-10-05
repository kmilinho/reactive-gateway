package com.kasasa.reactivegateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * This class represent an internal service from where Reactive Gateway will fetch data.
 * A route can contain several Endpoints, meaning that the response of such route is composed by
 * data coming from different endpoints.
 */
public class Endpoint {

    private String path;
    private String serviceId;
    private HttpMethod method;
    private List<String> inputParameters;
    private List<String> outputParameters;
}
