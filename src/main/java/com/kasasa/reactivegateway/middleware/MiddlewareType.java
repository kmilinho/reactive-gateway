
package com.kasasa.reactivegateway.middleware;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MiddlewareType {

    @JsonProperty("auth_header")
    AUTH_HEADER,

    @JsonProperty("logger")
    LOGGER
}
