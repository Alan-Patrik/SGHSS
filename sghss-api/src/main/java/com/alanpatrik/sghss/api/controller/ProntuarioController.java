package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.ProntuarioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.ProntuarioResponseDTO;
import com.alanpatrik.sghss.api.service.ProntuarioService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ProntuarioResponseDTO> getById(
            @Parameter(example = "id", description = "Id do prontuário cadastrado", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(prontuarioService.findById(id));
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.CREATE_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ProntuarioResponseDTO> create(
            @Parameter(example = "Prontuario", description = "Objeto Prontuario", required = true, name = "prontuarioRequestDTO") @RequestBody ProntuarioRequestDTO prontuarioRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(prontuarioService.save(prontuarioRequestDTO));
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_ATUALIZAR_PRONTUARIO + "')")
    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ProntuarioResponseDTO> update(
            @Parameter(example = "id", description = "Id do prontuário cadastrado", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "Prontuario", description = "Objeto Prontuario", required = true, name = "prontuarioRequestDTO") @RequestBody ProntuarioRequestDTO prontuarioRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(prontuarioService.update(id, prontuarioRequestDTO));
    }
}
