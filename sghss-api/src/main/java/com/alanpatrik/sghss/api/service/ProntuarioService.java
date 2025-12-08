package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.model.dto.request.ProntuarioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.ProntuarioResponseDTO;
import com.alanpatrik.sghss.api.repository.ProntuarioRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final PacienteService pacienteService;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public ProntuarioResponseDTO findById(Long id) {
        var prontuario = prontuarioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRONTUARIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        return Prontuario.toResponseDTO(prontuario);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
                .prescricoes(new LinkedHashSet<>())
                .paciente(paciente)
                .build();

        prontuario = prontuarioRepository.save(prontuario);

        return Prontuario.toResponseDTO(prontuario);
    }

    @RequireRoles({Constantes.PRIV_ATUALIZAR_PRONTUARIO})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var prontuario = prontuarioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRONTUARIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        if (prontuario.getPrescricoes() != null && !prontuario.getPrescricoes().isEmpty()) {
            for (var prescricao : new ArrayList<>(prontuario.getPrescricoes())) {
                prescricao.setProntuario(null);
            }
            prontuario.getPrescricoes().clear();
        }

        var paciente = prontuario.getPaciente();
        if (paciente != null) {
            prontuario.setPaciente(null);
            paciente.setProntuario(null);
        }

        prontuarioRepository.deleteById(prontuario.getId());
        return "Prontuario deletado com sucesso!";
    }

    private void validarParametrosObrigatorios(ProntuarioRequestDTO rontuarioRequestDTO) {
        if (rontuarioRequestDTO.getNomePaciente() == null || rontuarioRequestDTO.getNomePaciente().isEmpty()) {
            throw new ParametroInvalidoException("O campo Nome é obrigatório.");
        }
    }
}
