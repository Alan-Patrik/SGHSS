package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.UnidadeSaudeRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.UnidadeSaudeResponseDTO;
import com.alanpatrik.sghss.api.service.UnidadeSaudeService;
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
@RequestMapping("/api/v1/unidade-saude")
public class UnidadeServicoController {

    private final UnidadeSaudeService unidadeSaudeService;

    // TODO
    // FAZER AUTENTICAÇÃO NO SISTEMA
    // METODO SERÁ SOMENTE PARA O ADM DO SISTEMA
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<UnidadeSaudeResponseDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(unidadeSaudeService.getAll());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<UnidadeSaudeResponseDTO> getById(
            @Parameter(example = "id", description = "Id da Unidade de Saúde cadastrada", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(unidadeSaudeService.findById(id));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<UnidadeSaudeResponseDTO> save(
            @Parameter(example = "UnidadeSaude", description = "Objeto Unidade de Saúde", required = true, name = "unidadeSaudeRequestDTO") @RequestBody UnidadeSaudeRequestDTO unidadeSaudeRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(unidadeSaudeService.save(unidadeSaudeRequestDTO));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<UnidadeSaudeResponseDTO> update(
            @Parameter(example = "id", description = "Id da Unidade de Saúde cadastrada", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "UnidadeSaude", description = "Objeto Unidade de Saúde", required = true, name = "unidadeSaudeRequestDTO") @RequestBody UnidadeSaudeRequestDTO unidadeSaudeRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(unidadeSaudeService.update(id, unidadeSaudeRequestDTO));
    }

    // TODO
    // FAZER AUTENTICAÇÃO NO SISTEMA
    // METODO SERÁ SOMENTE PARA O ADM DO SISTEMA
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public void delete(
            @Parameter(example = "id", description = "Id da Unidade de Saúde cadastrada", required = true) @PathVariable(name = "id") Long id
    ) {
        unidadeSaudeService.delete(id);
        ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

