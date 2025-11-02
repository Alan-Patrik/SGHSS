package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.repository.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private PacienteService pacienteService;

    public Prontuario findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prontuário não encontrado!");
        }
        return prontuarioRepository.findById(id).get();
    }

    private boolean verifyIfExistsById(Long id) {
        return prontuarioRepository.existsById(id);
    }

    private boolean verifyIfContainsByObject(Prontuario prontuario) {
        return prontuarioRepository.findAll().contains(prontuario);
    }

    public Prontuario save(Long idPaciente, Prontuario prontuario) throws Exception {
        if (verifyIfContainsByObject(prontuario)) {
            throw new Exception("Prontuário já cadastrado!");
        }

        if (pacienteService.findById(idPaciente) == null) {
            throw new Exception("Paciente não encontrado!");
        }

        var paciente = pacienteService.findById(idPaciente);
        var novoProntuario = Prontuario.builder()
                .data(prontuario.getData())
                .observacao(prontuario.getObservacao())
                .paciente(paciente)
                .build();

        prontuarioRepository.save(novoProntuario);
        return novoProntuario;
    }

    public Prontuario update(Long id, Prontuario prontuario) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prontuário não encontrado!");
        }

        var prontuarioAtualizado = prontuarioRepository.findById(id).get();
        prontuarioAtualizado.setData(prontuario.getData());
        prontuarioAtualizado.setObservacao(prontuario.getObservacao());

        prontuarioRepository.save(prontuarioAtualizado);
        return prontuarioAtualizado;
    }
}
