package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.AgendaRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.AgendaResponseDTO;
import com.alanpatrik.sghss.api.service.AgendaService;
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

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/agendas")
public class AgendaController {

    private final AgendaService agendaService;

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_AGENDAS + "')")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<AgendaResponseDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(agendaService.getAll());
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_AGENDAS + "')")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<AgendaResponseDTO> getById(
            @Parameter(example = "id", description = "Id da agenda cadastrada", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(agendaService.findById(id));
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_AGENDAS + "')")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<AgendaResponseDTO> save(
            @Parameter(example = "Agenda", description = "Objeto Agenda do Profissional de Saúde", required = true, name = "agendaRequestDTO") @RequestBody AgendaRequestDTO agenda
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendaService.save(agenda));
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_AGENDAS + "')")
    @PostMapping("/{id}/horario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<AgendaResponseDTO> addTime(
            @Parameter(example = "id", description = "Id da agenda cadastrada", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "dataHoraNovaConsulta", description = "Data e hora da nova consulta", required = true, name = "dataHoraNovaConsulta") @RequestParam LocalDateTime dataHoraNovaConsulta
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendaService.addTime(id, dataHoraNovaConsulta));
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_AGENDAS + "')")
    @PostMapping("/{id}/horario/agendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<AgendaResponseDTO> schedule(
            @Parameter(example = "id", description = "Id da agenda cadastrada", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "dataHoraConsulta", description = "Data e hora da consulta", required = true, name = "dataHoraConsulta") @RequestParam LocalDateTime dataHoraConsulta
    ) {
        agendaService.schedule(id, dataHoraConsulta);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_AGENDAS + "')")
    @PutMapping("/{id}/horario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<AgendaResponseDTO> updateTime(
            @Parameter(example = "id", description = "Id da agenda cadastrada", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "dataHoraConsultaAntiga", description = "Data e hora da consulta antiga", required = true, name = "dataHoraConsultaAntiga") @RequestParam LocalDateTime dataHoraConsultaAntiga,
            @Parameter(example = "dataHoraNovaConsulta", description = "Data e hora da nova consulta", required = true, name = "dataHoraNovaConsulta") @RequestParam LocalDateTime dataHoraNovaConsulta
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(agendaService.updateTime(id, dataHoraConsultaAntiga, dataHoraNovaConsulta));
    }

    @PreAuthorize("hasAuthority('" + Constantes.PRIV_GERENCIAR_AGENDAS + "')")
    @DeleteMapping("/{id}/horario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<Void> deleteTime(
            @Parameter(example = "id", description = "Id da agenda cadastrada", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "dataHoraConsulta", description = "Data e hora da consulta", required = true, name = "dataHoraConsulta") @RequestParam LocalDateTime dataHoraConsulta
    ) {
        agendaService.deleteTime(id, dataHoraConsulta);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

