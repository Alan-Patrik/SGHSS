package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.AuditoriaDTO;
import com.alanpatrik.sghss.api.model.dto.ErroDTO;
import com.alanpatrik.sghss.api.model.dto.request.AuditoriaRequestDTO;
import com.alanpatrik.sghss.api.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] r = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<Page<AuditoriaDTO>> search(
            @Parameter(example = "username", name = "username") @RequestParam(required = false) String username,
            @Parameter(example = "action", name = "action") @RequestParam(required = false) String action,
            @Parameter(example = "resource", name = "resource") @RequestParam(required = false) String resource,
            @Parameter(example = "from", name = "from") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(example = "to", name = "to") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @Parameter(example = "page", name = "page") @RequestParam(defaultValue = "0") int page,
            @Parameter(example = "size", name = "size") @RequestParam(defaultValue = "20") int size,
            @Parameter(example = "sort", name = "sort") @RequestParam(defaultValue = "eventTime,desc") String sort
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(auditoriaService.search(username, action, resource, from, to, page, size, sort));
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<AuditoriaDTO> findOne(
            @Parameter(example = "id", description = "Id da Auditoria cadastrada", required = true) @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(auditoriaService.findOne(id));
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv(
            @Parameter(example = "username", name = "username") @RequestParam(required = false) String username,
            @Parameter(example = "action", name = "action") @RequestParam(required = false) String action,
            @Parameter(example = "resource", name = "resource") @RequestParam(required = false) String resource,
            @Parameter(example = "from", name = "from") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(example = "to", name = "to") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @Parameter(example = "sort", name = "sort") @RequestParam(defaultValue = "eventTime,desc") String sort

    ) {
        var csv = auditoriaService.exportCsv(username, action, resource, from, to, sort);
        // Adiciona BOM para Excel reconhecer UTF-8
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] body = concat(bom, csv.getBytes(StandardCharsets.UTF_8));

        var headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"auditoria.csv\"");
        headers.setContentLength(body.length);

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = Constantes.BAD_REQUEST_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = Constantes.NOT_FOUND_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = Constantes.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<AuditoriaDTO> create(
            @Parameter(example = "auditoriaRequestDTO", description = "Objeto Unidade de Saúde", required = true, name = "auditoriaRequestDTO") @RequestBody AuditoriaRequestDTO auditoriaRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(auditoriaService.create(auditoriaRequestDTO));
    }

    @GetMapping("/recent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constantes.OK_MESSAGE, useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = Constantes.ERRO_MESSAGE, content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<List<AuditoriaDTO>> recent(
            @Parameter(example = "limit", required = true, name = "limit") @RequestParam(defaultValue = "10") @Pattern(regexp = "^[1-9][0-9]*$", message = "limit deve ser um inteiro > 0") String limit
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(auditoriaService.recent(Integer.parseInt(limit)));
    }
}

