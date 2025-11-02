package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.dto.PrescricaoDTO;
import com.alanpatrik.sghss.api.service.PrescricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prescricoes")
public class PrescricaoController {

    @Autowired
    private PrescricaoService prescricaoService;

    @GetMapping("/{id}")
    public PrescricaoDTO getById(@PathVariable Long id) throws Exception {
        return prescricaoService.findById(id);
    }

    @PostMapping
    public PrescricaoDTO create(@RequestBody PrescricaoDTO prescricaoDTO) throws Exception {
        return prescricaoService.save(prescricaoDTO);
    }

    @PutMapping("/{id}")
    public PrescricaoDTO update(@PathVariable Long id, @RequestBody PrescricaoDTO prescricaoDTO) throws Exception {
        return prescricaoService.update(id, prescricaoDTO);
    }
}
