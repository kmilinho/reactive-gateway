package com.kasasa.reactivegateway.Service;

import com.kasasa.reactivegateway.Service.Middleware.MiddlewareType;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    private String id;
    private String resolveMethod;
    private ResolveInfo resolveInfo;
    private List<MiddlewareType> inputMiddleware;
    private List<MiddlewareType> outputMiddleware;
}
