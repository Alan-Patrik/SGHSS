package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.model.dto.LoginDTO;
import com.alanpatrik.sghss.api.model.dto.request.LoginRequestDTO;
import com.alanpatrik.sghss.api.service.AuthService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(
            @Parameter(example = "loginRequestDTO", description = "Objeto Login", required = true, name = "loginRequestDTO") @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(loginRequestDTO));
    }
}
