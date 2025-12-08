package com.alanpatrik.sghss.api.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditoriaDTO {

    private Long id;
    private LocalDateTime eventTime;
    private String username;
    private String action;
    private String resource;
    private String httpMethod;
    private String ip;
    private String userAgent;
    private String details;

}
