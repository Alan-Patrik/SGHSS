package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.request.PrivilegioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.PrivilegioResponseDTO;
import com.alanpatrik.sghss.api.service.PrivilegioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/privilegios")
public class PrivilegioController {

    private final PrivilegioService privilegioService;

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @GetMapping
    public ResponseEntity<List<PrivilegioResponseDTO>> getAll() {
        return ResponseEntity.ok(privilegioService.findAll());
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @GetMapping("/nome")
    public ResponseEntity<PrivilegioResponseDTO> getByName(@RequestParam("name") String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(privilegioService.findByName(name));
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @PostMapping
    public ResponseEntity<PrivilegioResponseDTO> create(@RequestBody PrivilegioRequestDTO privilegioRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(privilegioService.createPrivilege(privilegioRequestDTO));
    }
}

