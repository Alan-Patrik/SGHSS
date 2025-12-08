package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.LeitoDTO;
import com.alanpatrik.sghss.api.model.dto.request.LeitoAdicionarPacienteRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.LeitoAdicionarProfissionalRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.LeitoRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.LeitoUpdateRequestDTO;
import com.alanpatrik.sghss.api.service.LeitoService;
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
@RequestMapping("/api/v1/leitos")
public class LeitoController {

    private final LeitoService leitoService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<LeitoDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(leitoService.getAll());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<LeitoDTO> getById(
            @Parameter(example = "id", description = "Id do Leito da Unidade de Saúde", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(leitoService.findById(id));
    }

    @GetMapping("/numero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<LeitoDTO> getByNumber(
            @Parameter(example = "numero", description = "Número do Leito da Unidade de Saúde", required = true) @RequestParam(name = "numero") String numero
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(leitoService.findByNumero(numero));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<LeitoDTO> save(
            @Parameter(example = "Leito", description = "Objeto Leito", required = true, name = "leitoRequestDTO") @RequestBody LeitoRequestDTO leitoRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leitoService.save(leitoRequestDTO));
    }

    @PostMapping("/adicionar-profissional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<LeitoDTO> addProfissionalSaude(
            @Parameter(example = "LeitoAdicionarProfissionalRequestDTO", description = "Objeto LeitoAdicionarProfissionalRequestDTO", required = true, name = "LeitoAdicionarProfissionalRequestDTO") @RequestBody LeitoAdicionarProfissionalRequestDTO leitoAdicionarProfissionalRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leitoService.addProfissionalSaude(leitoAdicionarProfissionalRequestDTO));
    }

    @PostMapping("/adicionar-paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<LeitoDTO> addPaciente(
            @Parameter(example = "LeitoAdicionarPacienteRequestDTO", description = "Objeto LeitoAdicionarPacienteRequestDTO", required = true, name = "LeitoAdicionarPacienteRequestDTO") @RequestBody LeitoAdicionarPacienteRequestDTO leitoAdicionarPacienteRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leitoService.addPaciente(leitoAdicionarPacienteRequestDTO));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<LeitoDTO> update(
            @Parameter(example = "id", description = "Id do Leito da Unidade de Saúde", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "Leito", description = "Objeto Leito", required = true, name = "leitoRequestDTO") @RequestBody LeitoUpdateRequestDTO leitoUpdateRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(leitoService.update(id, leitoUpdateRequestDTO));
    }

    @DeleteMapping("/remover-profissional/{crm}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> removerProfissionalSaude(
            @Parameter(example = "crm", description = "CRM do Profissional de Saúde", required = true) @PathVariable(name = "crm") String crm,
            @Parameter(example = "numeroLeito", description = "Número do Leito da unidade de saúde", required = true) @RequestParam(name = "numeroLeito") String numeroLeito
    ) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(leitoService.removerProfissionalSaude(crm, numeroLeito));
    }

    @DeleteMapping("/remover-paciente/{nomePaciente}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<String> removerPaciente(
            @Parameter(example = "nomePaciente", description = "Nome do Paciente", required = true) @PathVariable(name = "nomePaciente") String nomePaciente,
            @Parameter(example = "numeroLeito", description = "Número do Leito da unidade de saúde", required = true) @RequestParam(name = "numeroLeito") String numeroLeito
    ) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(leitoService.removerPaciente(nomePaciente, numeroLeito));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constantes.NO_CONTENT_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(example = "id", description = "Id do Leito da Unidade de Saúde", required = true) @PathVariable(name = "id") Long id
    ) {
        leitoService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

