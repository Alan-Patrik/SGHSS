package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.dto.ProntuarioDTO;
import com.alanpatrik.sghss.api.model.Paciente;
import com.alanpatrik.sghss.api.model.Prescricao;
import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.repository.PacienteRepository;
import com.alanpatrik.sghss.api.repository.PrescricaoRepository;
import com.alanpatrik.sghss.api.repository.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private PrescricaoRepository prescricaoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public ProntuarioDTO findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prontuário não encontrado!");
        }
        return Prontuario.toDTO(prontuarioRepository.findById(id).get());
    }

    private boolean verifyIfExistsById(Long id) {
        return prontuarioRepository.existsById(id);
    }

    private boolean verificaSePacientePossuiProntuario(Paciente paciente) {
        return !paciente.getProntuarios().isEmpty();
    }

    public ProntuarioDTO save(ProntuarioDTO prontuarioDTO) throws Exception {
        if (!pacienteRepository.existsPacienteByNome(prontuarioDTO.getNomePaciente())) {
            throw new Exception("Paciente não encontrado!");
        }

        var paciente = pacienteRepository.findByNome(prontuarioDTO.getNomePaciente());

        if (verificaSePacientePossuiProntuario(paciente)) {
            throw new Exception("Prontuário já cadastrado!");
        }

        var prontuario = Prontuario.builder()
                .dataModificacao(LocalDate.now())
                .observacao(prontuarioDTO.getObservacao())
                .paciente(paciente)
                .prescricoes(Prescricao.toEntityDTOLis(prontuarioDTO.getPrescricoes()))
                .build();

        prontuario = prontuarioRepository.save(prontuario);

        prescricaoRepository.saveAll(prontuario.getPrescricoes());
        return Prontuario.toDTO(prontuario);
    }

    public ProntuarioDTO update(Long id, ProntuarioDTO prontuarioDTO) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prontuário não encontrado!");
        }

        var prontuario = prontuarioRepository.findById(id).get();
        prontuario.setDataModificacao(LocalDate.now());
        prontuario.setObservacao(prontuarioDTO.getObservacao());

        prontuario = prontuarioRepository.save(prontuario);
        return Prontuario.toDTO(prontuario);
    }
}
