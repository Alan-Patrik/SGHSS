package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.dto.request.PacienteRequestDTO;
import com.alanpatrik.sghss.api.dto.response.PacienteResponseDTO;
import com.alanpatrik.sghss.api.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(pacienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(pacienteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> create(@RequestBody PacienteRequestDTO pacienteRequestDTO) throws Exception {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(pacienteService.save(pacienteRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> update(@PathVariable Long id, @RequestBody PacienteRequestDTO pacienteRequestDTO) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(pacienteService.update(id, pacienteRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws Exception {
        pacienteService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT.value()).build();
    }
}
