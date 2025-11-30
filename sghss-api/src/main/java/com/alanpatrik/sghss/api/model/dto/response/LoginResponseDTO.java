package com.alanpatrik.sghss.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private long expiresInMinutes;
}
