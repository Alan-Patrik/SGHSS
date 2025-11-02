package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.dto.request.ProntuarioRequestDTO;
import com.alanpatrik.sghss.api.dto.response.ProntuarioResponseDTO;
import com.alanpatrik.sghss.api.model.Paciente;
import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.repository.PacienteRepository;
import com.alanpatrik.sghss.api.repository.PrescricaoRepository;
import com.alanpatrik.sghss.api.repository.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private PrescricaoRepository prescricaoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public ProntuarioResponseDTO findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prontuário não encontrado!");
        }
        return Prontuario.toResponseDTO(prontuarioRepository.findById(id).get());
    }

    private boolean verifyIfExistsById(Long id) {
        return prontuarioRepository.existsById(id);
    }

    private boolean verificaSePacientePossuiProntuario(Paciente paciente) {
        return !paciente.getProntuarios().isEmpty();
    }

    public ProntuarioResponseDTO save(ProntuarioRequestDTO prontuarioRequestDTO) throws Exception {
        if (!pacienteRepository.existsPacienteByNome(prontuarioRequestDTO.getNomePaciente())) {
            throw new Exception("Paciente não encontrado!");
        }

        var paciente = pacienteRepository.findByNome(prontuarioRequestDTO.getNomePaciente());

        if (verificaSePacientePossuiProntuario(paciente)) {
            throw new Exception("Prontuário já cadastrado!");
        }

        var prontuario = Prontuario.builder()
                .observacao(prontuarioRequestDTO.getObservacao())
                .dataCriacao(LocalDateTime.now())
                .dataModificacao(LocalDateTime.now())
                .paciente(paciente)
                .prescricoes(new ArrayList<>())
                .build();

        prontuario = prontuarioRepository.save(prontuario);

        return Prontuario.toResponseDTO(prontuario);
    }

    public ProntuarioResponseDTO update(Long id, ProntuarioRequestDTO prontuarioRequestDTO) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prontuário não encontrado!");
        }

        var prontuario = prontuarioRepository.findById(id).get();
        prontuario.setDataModificacao(LocalDateTime.now());
        prontuario.setObservacao(prontuarioRequestDTO.getObservacao());

        prontuario = prontuarioRepository.save(prontuario);
        return Prontuario.toResponseDTO(prontuario);
    }
}
