package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.model.dto.request.RoleRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.alanpatrik.sghss.api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/authority")
    public ResponseEntity<RoleResponseDTO> findByAuthority(@RequestParam("authority") String authority) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleService.findByAuthority(authority));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@RequestBody RoleRequestDTO roleRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleService.createRole(roleRequestDTO));
    }

    @PostMapping("/{roleId}/privilegios/{privilegioId}")
    public ResponseEntity<Void> addPrivilegio(@PathVariable long roleId,
                                              @PathVariable long privilegioId) {
        roleService.addPrivilegioToRole(roleId, privilegioId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{roleId}/privilegios/{privilegioId}")
    public ResponseEntity<Void> removePrivilegio(@PathVariable long roleId,
                                                 @PathVariable long privilegioId) {
        roleService.removePrivilegeFromRole(roleId, privilegioId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

