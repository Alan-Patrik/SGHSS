package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.PagamentoRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.PagamentoResponseDTO;
import com.alanpatrik.sghss.api.service.PagamentoService;
import com.alanpatrik.sghss.api.service.RelatorioFinanceiroService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/financeiro")
public class FinanceiroController {

    private final PagamentoService pagamentoService;
    private final RelatorioFinanceiroService relatorioFinanceiroService;

    @PostMapping("/consultas/{id}/pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<PagamentoResponseDTO> pagarConsulta(
            @Parameter(example = "id", description = "Id da consulta", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "Pagamento", description = "Objeto Pagamento", required = true, name = "pagamentoRequestDTO") @RequestBody PagamentoRequestDTO pagamentoRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pagamentoService.pagarConsulta(id, pagamentoRequestDTO));
    }

    @PostMapping("/exames/{id}/pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<PagamentoResponseDTO> pagarExame(
            @Parameter(example = "id", description = "Id da consulta", required = true) @PathVariable(name = "id") Long id,
            @Parameter(example = "Pagamento", description = "Objeto Pagamento", required = true, name = "pagamentoRequestDTO") @RequestBody PagamentoRequestDTO pagamentoRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pagamentoService.pagarExame(id, pagamentoRequestDTO));
    }

    @GetMapping(value = "/faturamento-mensal/export/csv", produces = "text/csv")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<byte[]> exportMensalCsv(
            @Parameter(example = "Data-inicio", required = true) @RequestParam(name = "inicio") LocalDateTime inicio,
            @Parameter(example = "Data-fim", required = true, name = "fim") @RequestParam LocalDateTime fim
    ) {
        var arquivoExportadoDTO = relatorioFinanceiroService.exportarFaturamentoMensalCsv(inicio.toString(), fim.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Disposition", "attachment; filename=" + arquivoExportadoDTO.getNomeArquivo())
                .contentType(org.springframework.http.MediaType.parseMediaType(arquivoExportadoDTO.getContentType()))
                .body(arquivoExportadoDTO.getConteudo());
    }

    @GetMapping(value = "/faturamento-mensal/export/xlsx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<byte[]> exportMensalXlsx(
            @Parameter(example = "Data-inicio", required = true) @RequestParam(name = "inicio") LocalDateTime inicio,
            @Parameter(example = "Data-fim", required = true, name = "fim") @RequestParam LocalDateTime fim
    ) {
        var arquivoExportadoDTO = relatorioFinanceiroService.exportarFaturamentoMensalXlsx(inicio.toString(), fim.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Disposition", "attachment; filename=" + arquivoExportadoDTO.getNomeArquivo())
                .contentType(org.springframework.http.MediaType.parseMediaType(arquivoExportadoDTO.getContentType()))
                .body(arquivoExportadoDTO.getConteudo());
    }
}
