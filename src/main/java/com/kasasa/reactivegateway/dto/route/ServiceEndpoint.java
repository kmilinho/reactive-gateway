package com.kasasa.reactivegateway.dto.route;

import com.kasasa.reactivegateway.dto.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEndpoint {
    private String serviceId;
    private String endpointPath;

    private List<Parameter> inputParameters;
    private List<Parameter> outputParameters;
}
