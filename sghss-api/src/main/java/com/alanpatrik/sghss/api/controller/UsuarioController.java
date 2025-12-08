package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.UsuarioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.UsuarioUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.UsuarioResponseDTO;
import com.alanpatrik.sghss.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.findAll());
    }

    @GetMapping("/username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> findByUsername(
            @Parameter(example = "username", description = "Username do usuário cadastrado", required = true) @RequestParam String username
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.findByUsername(username));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> create(
            @Parameter(example = "usuarioRequestDTO", description = "Objeto Usuário", required = true, name = "usuarioRequestDTO") @RequestBody UsuarioRequestDTO usuarioRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.save(usuarioRequestDTO));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> assignRole(
            @Parameter(example = "userId", description = "Id do Usuário cadastrado", required = true) @RequestParam(name = "userId") Long userId,
            @Parameter(example = "roleId", description = "Id da Role cadastrada", required = true) @RequestParam(name = "roleId") Long roleId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.assignRoleToUser(userId, roleId));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> update(
            @Parameter(example = "id", description = "Id do Usuário cadastrado", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "usuarioUpdateRequestDTO", description = "Objeto Usuário", required = true, name = "usuarioUpdateRequestDTO") @RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.update(id, usuarioUpdateRequestDTO));
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
                .body(usuarioService.delete(id));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> removeRole(
            @Parameter(example = "userId", description = "Id do Usuário cadastrado", required = true) @RequestParam(name = "userId") Long userId,
            @Parameter(example = "roleId", description = "Id da Role cadastrada", required = true) @RequestParam(name = "roleId") Long roleId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.removeRoleFromUser(userId, roleId));
    }
}

