package com.kasasa.reactivegateway.dto.service;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResolveInfo {
    private String url;
    private String dns;
}
