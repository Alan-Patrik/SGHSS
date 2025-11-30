package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.request.RoleRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.alanpatrik.sghss.api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @GetMapping("/authority")
    public ResponseEntity<RoleResponseDTO> findByAuthority(@RequestParam("authority") String authority) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleService.findByAuthority(authority));
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@RequestBody RoleRequestDTO roleRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleService.createRole(roleRequestDTO));
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @PostMapping("/{roleId}/privilegios/{privilegioId}")
    public ResponseEntity<Void> addPrivilegio(@PathVariable long roleId,
                                              @PathVariable long privilegioId) {
        roleService.addPrivilegioToRole(roleId, privilegioId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @DeleteMapping("/{roleId}/privilegios/{privilegioId}")
    public ResponseEntity<Void> removePrivilegio(@PathVariable long roleId,
                                                 @PathVariable long privilegioId) {
        roleService.removePrivilegeFromRole(roleId, privilegioId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

