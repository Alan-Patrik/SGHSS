package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.model.Prescricao;
import com.alanpatrik.sghss.api.service.PrescricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prescricoes")
public class PrescricaoController {

    @Autowired
    private PrescricaoService prescricaoService;

    @GetMapping("/{id}")
    public Prescricao getById(@PathVariable Long id) throws Exception {
        return prescricaoService.findById(id);
    }

    @PostMapping("/{id}")
    public Prescricao create(@PathVariable Long id, @RequestBody Prescricao prescricao) throws Exception {
        return prescricaoService.save(id, prescricao);
    }

    @PutMapping("/{id}")
    public Prescricao update(@PathVariable Long id, @RequestBody Prescricao prescricao) throws Exception {
        return prescricaoService.update(id, prescricao);
    }
}
