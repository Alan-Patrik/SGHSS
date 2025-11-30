package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.ConsultaRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.ConsultaUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.ConsultaResponseDTO;
import com.alanpatrik.sghss.api.service.ConsultaService;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<ConsultaResponseDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.getAll());
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_CADASTROS + "')")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ConsultaResponseDTO> getById(
            @Parameter(example = "id", description = "Id da consulta", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.findById(id));
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_AGENDAR_CONSULTA + "')")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ConsultaResponseDTO> save(
            @Parameter(example = "Consulta", description = "Objeto Consulta", required = true, name = "consultaRequestDTO") @RequestBody ConsultaRequestDTO consultaRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(consultaService.save(consultaRequestDTO));
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_CADASTROS + "')")
    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ConsultaResponseDTO> update(
            @Parameter(example = "id", description = "Id da consulta", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "Consulta", description = "Objeto Consulta", required = true, name = "consultaUpdateRequestDTO") @RequestBody ConsultaUpdateRequestDTO consultaUpdateRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.update(id, consultaUpdateRequestDTO));
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_CANCELAR_CONSULTA + "')")
    @PostMapping("/cancelar-consulta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> cancel(
            @Parameter(example = "Consulta", description = "Objeto Consulta", required = true, name = "consultaRequestDTO") @RequestBody ConsultaRequestDTO consultaRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.cancel(consultaRequestDTO));
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(example = "id", description = "Id da agenda cadastrada", required = true) @PathVariable(name = "id") Long id
    ) {
        consultaService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

