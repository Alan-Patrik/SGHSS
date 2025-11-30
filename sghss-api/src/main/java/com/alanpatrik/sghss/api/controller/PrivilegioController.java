package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.model.dto.request.PrivilegioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.PrivilegioResponseDTO;
import com.alanpatrik.sghss.api.service.PrivilegioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/privilegios")
public class PrivilegioController {

    private final PrivilegioService privilegioService;

    @GetMapping
    public ResponseEntity<List<PrivilegioResponseDTO>> list() {
        return ResponseEntity.ok(privilegioService.findAll());
    }

    @GetMapping("/nome")
    public ResponseEntity<PrivilegioResponseDTO> findByName(@RequestParam("name") String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(privilegioService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<PrivilegioResponseDTO> create(@RequestBody PrivilegioRequestDTO privilegioRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(privilegioService.createPrivilege(privilegioRequestDTO));
    }

}

