package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.dto.request.LoginRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.LoginResponseDTO;
import com.alanpatrik.sghss.api.security.jwt.JwtProperties;
import com.alanpatrik.sghss.api.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenService tokenService;
    private final JwtProperties jwtProperties;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        this.validarParametrosObrigatorios(loginRequestDTO);

        var authenticate = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

        var user = (UserDetails) authenticate.getPrincipal();
        String token = tokenService.generateToken(user);
        return new LoginResponseDTO(token, jwtProperties.getExpirationMinutes());
    }

    private void validarParametrosObrigatorios(LoginRequestDTO loginRequestDTO) {
        if (loginRequestDTO.getUsername() == null ||
                loginRequestDTO.getUsername().isEmpty() ||
                loginRequestDTO.getUsername().isBlank()) {
            throw new ParametroInvalidoException("O Campo Username é obrigatório.");
        }

        if (loginRequestDTO.getPassword() == null ||
                loginRequestDTO.getPassword().isEmpty() ||
                loginRequestDTO.getPassword().isBlank()) {
            throw new ParametroInvalidoException("O Campo Password é obrigatório.");
        }
    }
}
