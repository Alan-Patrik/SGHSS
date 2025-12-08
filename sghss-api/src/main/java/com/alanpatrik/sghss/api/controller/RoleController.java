package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.RoleRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.alanpatrik.sghss.api.service.RoleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<RoleResponseDTO> create(
            @Parameter(example = "roleRequestDTO", description = "Objeto Role cadastrada", required = true, name = "roleRequestDTO") @RequestBody RoleRequestDTO roleRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleService.createRole(roleRequestDTO));
    }

    @PostMapping("/adicionar-privilegio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> addPrivilegio(
            @Parameter(example = "roleId", description = "Id da Role cadastrada", required = true) @RequestParam(name = "roleId") Long roleId,
            @Parameter(example = "privilegioId", description = "Id do Privilégio cadastrado", required = true) @RequestParam(name = "privilegioId") Long privilegioId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleService.addPrivilegioToRole(roleId, privilegioId));
    }

    @DeleteMapping("/privilegios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> removePrivilegio(
            @Parameter(example = "roleId", description = "Id da Role cadastrada", required = true) @RequestParam(name = "roleId") Long roleId,
            @Parameter(example = "privilegioId", description = "Id da Unidade de Saúde cadastrada", required = true) @RequestParam(name = "privilegioId") Long privilegioId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleService.removePrivilegeFromRole(roleId, privilegioId));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> delete(
            @Parameter(example = "id", description = "Id da Unidade de Saúde cadastrada", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleService.delete(id));
    }
}

