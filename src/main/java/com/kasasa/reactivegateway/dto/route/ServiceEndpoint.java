package com.kasasa.reactivegateway.dto.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEndpoint {
    private String serviceId;
    private String endpointPath;
}
