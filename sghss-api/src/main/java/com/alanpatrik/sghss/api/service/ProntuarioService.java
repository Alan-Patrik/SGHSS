package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.dto.request.ProntuarioRequestDTO;
import com.alanpatrik.sghss.api.dto.response.ProntuarioResponseDTO;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.repository.ProntuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final PacienteService pacienteService;

    public ProntuarioResponseDTO findById(Long id) {
        var prontuario = prontuarioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return Prontuario.toResponseDTO(prontuario);
    }

    public ProntuarioResponseDTO save(ProntuarioRequestDTO prontuarioRequestDTO) {
        this.validarParametrosObrigatorios(prontuarioRequestDTO);

        var paciente = pacienteService.findByName(prontuarioRequestDTO.getNomePaciente());
        if (paciente.getProntuario() != null) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        var prontuario = Prontuario.builder()
                .observacao(prontuarioRequestDTO.getObservacao())
                .dataCriacao(LocalDateTime.now())
                .dataModificacao(LocalDateTime.now())
                .prescricoes(new ArrayList<>())
                .paciente(paciente)
                .build();

        prontuario = prontuarioRepository.save(prontuario);

        return Prontuario.toResponseDTO(prontuario);
    }

    public ProntuarioResponseDTO update(Long id, ProntuarioRequestDTO prontuarioRequestDTO) {
        this.validarParametrosObrigatorios(prontuarioRequestDTO);

        var paciente = pacienteService.findByName(prontuarioRequestDTO.getNomePaciente());
        var prontuario = Prontuario.toEntity(this.findById(id));
        prontuario.setDataModificacao(LocalDateTime.now());
        prontuario.setObservacao(prontuarioRequestDTO.getObservacao());
        if (paciente.getProntuario() == null ||
                !paciente.getProntuario().getPaciente().getNome().equals(prontuarioRequestDTO.getNomePaciente())) {
            throw new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE);
        }
        prontuario.setPaciente(paciente);

        prontuario = prontuarioRepository.save(prontuario);
        return Prontuario.toResponseDTO(prontuario);
    }

    private void validarParametrosObrigatorios(ProntuarioRequestDTO rontuarioRequestDTO) {
        if (rontuarioRequestDTO.getNomePaciente() == null || rontuarioRequestDTO.getNomePaciente().isEmpty()) {
            throw new ParametroInvalidoException("O campo Nome é obrigatório.");
        }
    }
}
