
package com.kasasa.reactivegateway.middleware.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MiddlewareType {

    @JsonProperty("auth_header")
    AUTH_HEADER,

    @JsonProperty("logger")
    LOGGER
}
