package com.kasasa.reactivegateway.controller;

import com.kasasa.reactivegateway.dto.endpoint.Endpoint;
import com.kasasa.reactivegateway.dto.service.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatewayRequest {

    private Service service;
    private Endpoint endpoint;
    private Map<String, String> headers;
}
