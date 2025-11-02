package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.dto.request.PrescricaoRequestDTO;
import com.alanpatrik.sghss.api.dto.response.PrescricaoResponseDTO;
import com.alanpatrik.sghss.api.service.PrescricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prescricoes")
public class PrescricaoController {

    @Autowired
    private PrescricaoService prescricaoService;

    @GetMapping("/{id}")
    public ResponseEntity<PrescricaoResponseDTO> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(prescricaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PrescricaoResponseDTO> create(@RequestBody PrescricaoRequestDTO prescricaoRequestDTO) throws Exception {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(prescricaoService.save(prescricaoRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescricaoResponseDTO> update(@PathVariable Long id, @RequestBody PrescricaoRequestDTO prescricaoRequestDTO) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(prescricaoService.update(id, prescricaoRequestDTO));
    }
}
