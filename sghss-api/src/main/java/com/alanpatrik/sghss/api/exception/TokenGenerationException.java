package com.alanpatrik.sghss.api.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenGenerationException extends RuntimeException {

    private final String username;
    private final String roles;

    public TokenGenerationException(String message, String username, String roles, Throwable cause) {
        super(message, cause);
        this.username = username;
        this.roles = roles;
    }
}


