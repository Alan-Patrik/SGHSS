package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.dto.request.ProntuarioRequestDTO;
import com.alanpatrik.sghss.api.dto.response.ProntuarioResponseDTO;
import com.alanpatrik.sghss.api.service.ProntuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prontuarios")
public class ProntuarioController {

    @Autowired
    private ProntuarioService prontuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioResponseDTO> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(prontuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProntuarioResponseDTO> create(@RequestBody ProntuarioRequestDTO prontuarioRequestDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(prontuarioService.save(prontuarioRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProntuarioResponseDTO> update(@PathVariable Long id, @RequestBody ProntuarioRequestDTO prontuarioRequestDTO) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(prontuarioService.update(id, prontuarioRequestDTO));
    }
}
