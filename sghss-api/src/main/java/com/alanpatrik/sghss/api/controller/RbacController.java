package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.model.dto.response.PrivilegioResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.alanpatrik.sghss.api.service.RbacQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rbac")
public class RbacController {

    private final RbacQueryService rbacQueryService;

    @GetMapping("/usuarios/{username}/privilegios")
    @PreAuthorize("hasAuthority(T(com.alanpatrik.sghss.api.util.LogonUtil).LOGON_ROLE_ADMIN)")
    public ResponseEntity<Set<PrivilegioResponseDTO>> getPrivilegesOfUser(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rbacQueryService.getPrivilegesOfUser(username));
    }

    @GetMapping("/users/{username}/roles")
    public ResponseEntity<Set<RoleResponseDTO>> getRolesOfUser(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rbacQueryService.getRolesOfUser(username));
    }
}

