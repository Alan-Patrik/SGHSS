package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.dto.PacienteDTO;
import com.alanpatrik.sghss.api.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public List<PacienteDTO> getAll() {
        return pacienteService.findAll();
    }

    @GetMapping("/{id}")
    public PacienteDTO getById(@PathVariable Long id) throws Exception {
        return pacienteService.findById(id);
    }

    @GetMapping("/{id}/historico-clinico")
    public PacienteDTO getHistoricoClinicoById(@PathVariable Long id) {
        return pacienteService.findByHistoricoClinico(id);
    }

    @PostMapping
    public PacienteDTO create(@RequestBody PacienteDTO pacienteDTO) throws Exception {
        return pacienteService.save(pacienteDTO);
    }

    @PutMapping("/{id}")
    public PacienteDTO update(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO) throws Exception {
        return pacienteService.update(id, pacienteDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws Exception {
        pacienteService.delete(id);
    }
}
