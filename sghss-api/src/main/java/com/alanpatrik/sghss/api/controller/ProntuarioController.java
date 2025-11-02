package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.service.ProntuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prontuarios")
public class ProntuarioController {

    @Autowired
    private ProntuarioService prontuarioService;

    @GetMapping("/{id}")
    public Prontuario getById(@PathVariable Long id) throws Exception {
        return prontuarioService.findById(id);
    }

    @PostMapping("/{id}")
    public Prontuario create(@PathVariable Long id, @RequestBody Prontuario prontuario) throws Exception {
        return prontuarioService.save(id, prontuario);
    }

    @PutMapping("/{id}")
    public Prontuario update(@PathVariable Long id, @RequestBody Prontuario prontuario) throws Exception {
        return prontuarioService.update(id, prontuario);
    }
}
