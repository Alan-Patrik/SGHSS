package com.alanpatrik.sghss.api.controller;

import com.alanpatrik.sghss.api.model.Paciente;
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
    public List<Paciente> getAll() {
        return pacienteService.findAll();
    }
    
    @GetMapping("/{id}")
    public Paciente getPacienteById(@PathVariable Long id) throws Exception {
        return pacienteService.findById(id);
    }

    @PostMapping
    public Paciente create(@RequestBody Paciente paciente) throws Exception {
        return pacienteService.save(paciente);
    }

    @PutMapping("/{id}")
    public Paciente update(@PathVariable Long id, @RequestBody Paciente paciente) throws Exception {
        paciente.setId(id);
        return pacienteService.update(id, paciente);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws Exception {
        pacienteService.delete(id);
    }
}
