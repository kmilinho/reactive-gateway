package com.kasasa.reactivegateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EndpointResponseTuple {
    private Endpoint endpoint;
    private String endpointResponse;
    private List<Parameter> outputParameters;
}
