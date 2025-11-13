package com.alanpatrik.sghss.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ErroDTO {
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String instance;

}

