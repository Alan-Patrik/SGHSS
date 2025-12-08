package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.UnidadeSaudeDTO;
import com.alanpatrik.sghss.api.model.dto.request.UnidadeSaudeAdicionarProfissionalRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.UnidadeSaudeRequestDTO;
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
@RequestMapping("/api/v1/unidades-saude")
public class UnidadeSaudeController {

    private final UnidadeSaudeService unidadeSaudeService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<UnidadeSaudeDTO>> getAll() {
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
    public ResponseEntity<UnidadeSaudeDTO> getById(
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
    public ResponseEntity<UnidadeSaudeDTO> save(
            @Parameter(example = "unidadeSaudeRequestDTO", description = "Objeto Unidade de Saúde", required = true, name = "unidadeSaudeRequestDTO") @RequestBody UnidadeSaudeRequestDTO unidadeSaudeRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(unidadeSaudeService.save(unidadeSaudeRequestDTO));
    }

    @PostMapping("/adicionar-profissional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<UnidadeSaudeDTO> addProfissionalSaude(
            @Parameter(example = "unidadeSaudeAdicionarProfissionalRequestDTO", description = "Objeto Unidade de Saúde", required = true, name = "unidadeSaudeAdicionarProfissionalRequestDTO") @RequestBody UnidadeSaudeAdicionarProfissionalRequestDTO unidadeSaudeAdicionarProfissionalRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(unidadeSaudeService.addProfissionalSaude(unidadeSaudeAdicionarProfissionalRequestDTO));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<UnidadeSaudeDTO> update(
            @Parameter(example = "id", description = "Id da Unidade de Saúde cadastrada", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "unidadeSaudeRequestDTO", description = "Objeto Unidade de Saúde", required = true, name = "unidadeSaudeRequestDTO") @RequestBody UnidadeSaudeRequestDTO unidadeSaudeRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(unidadeSaudeService.update(id, unidadeSaudeRequestDTO));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> delete(
            @Parameter(example = "id", description = "Id da Unidade de Saúde cadastrada", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(unidadeSaudeService.delete(id));
    }

    @DeleteMapping("/remover-profissional/{crm}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<UnidadeSaudeDTO> deleteProfissionalSaude(
            @Parameter(example = "CRM", description = "CRM do Profissional de Saúde", required = true, name = "crm") @PathVariable String crm,
            @Parameter(example = "nomeUnidadeSaude", description = "Nome da unidade de Saúde", required = true, name = "nomeUnidadeSaude") @RequestParam String nomeUnidadeSaude
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(unidadeSaudeService.deleteProfissionalSaude(crm, nomeUnidadeSaude));
    }
}

