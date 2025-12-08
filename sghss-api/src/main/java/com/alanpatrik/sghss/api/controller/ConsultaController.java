package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ConsultaDTO;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.ConsultaRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.ConsultaUpdateRequestDTO;
import com.alanpatrik.sghss.api.service.ConsultaService;
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
@RequestMapping("/api/v1/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<ConsultaDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.getAll());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ConsultaDTO> getById(
            @Parameter(example = "id", description = "Id da consulta", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.findById(id));
    }


    @GetMapping("/{id}/join/{token}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> join(
            @Parameter(example = "id", required = true, name = "id") @PathVariable Long id,
            @Parameter(example = "token", description = "Token de acesso a consulta enviado no email", required = true, name = "token") @PathVariable String token
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.findByIdConsultaOnline(id, token));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ConsultaDTO> save(
            @Parameter(example = "Consulta", description = "Objeto Consulta", required = true, name = "consultaRequestDTO") @RequestBody ConsultaRequestDTO consultaRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(consultaService.save(consultaRequestDTO));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ConsultaDTO> update(
            @Parameter(example = "id", description = "Id da consulta", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "Consulta", description = "Objeto Consulta", required = true, name = "consultaUpdateRequestDTO") @RequestBody ConsultaUpdateRequestDTO consultaUpdateRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.update(id, consultaUpdateRequestDTO));
    }

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

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> delete(
            @Parameter(example = "id", description = "Id da agenda cadastrada", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(consultaService.delete(id));
    }
}

