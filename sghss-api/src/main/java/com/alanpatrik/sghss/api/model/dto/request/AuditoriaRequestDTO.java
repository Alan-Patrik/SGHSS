package com.alanpatrik.sghss.api.model.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditoriaRequestDTO {

    private String username;
    private String action;
    private String resource;
    private String ip;
    private String userAgent;
    private String details;
    private String httpMethod;
}
