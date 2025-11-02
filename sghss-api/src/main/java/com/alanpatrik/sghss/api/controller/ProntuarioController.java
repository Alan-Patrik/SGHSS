package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.dto.ProntuarioDTO;
import com.alanpatrik.sghss.api.service.ProntuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prontuarios")
public class ProntuarioController {

    @Autowired
    private ProntuarioService prontuarioService;

    @GetMapping("/{id}")
    public ProntuarioDTO getById(@PathVariable Long id) throws Exception {
        return prontuarioService.findById(id);
    }

    @PostMapping
    public ProntuarioDTO create(@RequestBody ProntuarioDTO prontuarioDTO) throws Exception {
        return prontuarioService.save(prontuarioDTO);
    }

    @PutMapping("/{id}")
    public ProntuarioDTO update(@PathVariable Long id, @RequestBody ProntuarioDTO prontuarioDTO) throws Exception {
        return prontuarioService.update(id, prontuarioDTO);
    }
}
