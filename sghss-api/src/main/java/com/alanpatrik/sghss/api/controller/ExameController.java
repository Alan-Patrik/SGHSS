package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.ExameRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.ExameUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.ExameResponseDTO;
import com.alanpatrik.sghss.api.service.ExameService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@RequestMapping("/api/v1/exames")
public class ExameController {

    private final ExameService exameService;

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<ExameResponseDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exameService.getAll());
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ExameResponseDTO> getById(
            @Parameter(example = "id", description = "Id do exame cadastrado", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exameService.findById(id));
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ExameResponseDTO> save(
            @Parameter(example = "Exame", description = "Objeto Exame", required = true, name = "exameRequestDTO") @RequestBody ExameRequestDTO exameRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(exameService.save(exameRequestDTO));
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<ExameResponseDTO> update(
            @Parameter(example = "id", description = "Id do exame cadastrado", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "exameUpdateRequestDTO", description = "Objeto exame", required = true, name = "exameUpdateRequestDTO") @RequestBody ExameUpdateRequestDTO exameUpdateRequestDTO
    ) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exameService.update(id, exameUpdateRequestDTO));
    }

    @PreAuthorize("hasAuthority('" + Constantes.LOGON_ROLE_ADMIN_SISTEMA + "')")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(example = "id", description = "Id do exame cadastrado", required = true) @PathVariable(name = "id") Long id
    ) {
        exameService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

