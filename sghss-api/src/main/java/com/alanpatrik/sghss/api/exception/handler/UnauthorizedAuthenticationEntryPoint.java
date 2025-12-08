package com.alanpatrik.sghss.api.exception.handler;

import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;

@Component
public class UnauthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public UnauthorizedAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var erro = new ErroDTO(
                OffsetDateTime.now().toLocalDateTime(),
                HttpStatus.UNAUTHORIZED.value(),
                "Usuário não autenticado ou credenciais inválidas.",
                request.getRequestURI()
        );

        ServletOutputStream responseStream = response.getOutputStream();
        this.mapper.writeValue(responseStream, erro);
        responseStream.flush();
    }
}
