package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.request.UsuarioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.UsuarioUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.UsuarioResponseDTO;
import com.alanpatrik.sghss.api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/username")
    public ResponseEntity<UsuarioResponseDTO> findByUsername(@RequestParam String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.findByUsername(username));
    }

    @PreAuthorize("hasAnyAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "','" + Constantes.LOGON_ROLE_ADMIN_CLINICA + "')")
    @PostMapping
    public ResponseEntity<String> create(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.save(usuarioRequestDTO));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> assignRole(@PathVariable Long userId,
                                           @PathVariable Long roleId) {
        usuarioService.assignRoleToUser(userId, roleId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.update(id, usuarioUpdateRequestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(usuarioService.delete(id));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRole(@PathVariable Long userId,
                                           @PathVariable Long roleId) {
        usuarioService.removeRoleFromUser(userId, roleId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


}

