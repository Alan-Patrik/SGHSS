package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Consulta;
import com.alanpatrik.sghss.api.model.dto.request.ConsultaRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.ConsultaUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.ConsultaResponseDTO;
import com.alanpatrik.sghss.api.model.enums.StatusConsulta;
import com.alanpatrik.sghss.api.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final AgendaService agendaService;
    private final ProfissionalSaudeService profissionalSaudeService;
    private final PacienteService pacienteService;

    public List<ConsultaResponseDTO> getAll() {
        return consultaRepository.findAll().stream().map(Consulta::toResponseDTO).toList();
    }

    public ConsultaResponseDTO findById(Long id) {
        var consulta = consultaRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return Consulta.toResponseDTO(consulta);
    }

    public ConsultaResponseDTO save(ConsultaRequestDTO consultaRequestDTO) {
        this.validarParametrosObrigatorios(consultaRequestDTO);
        var profissionalSaude = profissionalSaudeService.findByCRM(consultaRequestDTO.getCRM());
        var paciente = pacienteService.findByName(consultaRequestDTO.getNomePaciente());

        agendaService.schedule(profissionalSaude.getAgenda().getId(), consultaRequestDTO.getDataHoraConsulta());

        var consulta = Consulta.builder()
                .dataHoraConsulta(consultaRequestDTO.getDataHoraConsulta())
                .areaAtuacao(consultaRequestDTO.getAreaAtuacao())
                .tipoConsulta(consultaRequestDTO.getTipoConsulta())
                .profissionalSaude(profissionalSaude)
                .paciente(paciente)
                .statusConsulta(StatusConsulta.N)
                .build();

        consulta = consultaRepository.save(consulta);
        return Consulta.toResponseDTO(consulta);
    }

    public ConsultaResponseDTO update(Long id, ConsultaUpdateRequestDTO consultaUpdateRequestDTO) {
        this.validarParametrosObrigatorios(consultaUpdateRequestDTO);

        var consulta = Consulta.toEntity(this.findById(id));
        var profissionalSaude = profissionalSaudeService.findByCRM(consultaUpdateRequestDTO.getCRM());
        var paciente = pacienteService.findByName(consultaUpdateRequestDTO.getNomePaciente());

        if (!consulta.getStatusConsulta().equals(StatusConsulta.N)) {
            throw new InformacaoNaoEncontradaException("Não foi possível prosseguir. Consulta em andamento, já finalizada ou cancelada.");
        }

        agendaService.updateTime(
                profissionalSaude.getAgenda().getId(),
                consultaUpdateRequestDTO.getDataHoraConsultaAntiga(),
                consultaUpdateRequestDTO.getDataHoraNovaConsulta());

        consulta.setDataHoraConsulta(consultaUpdateRequestDTO.getDataHoraNovaConsulta());
        consulta.setAreaAtuacao(consultaUpdateRequestDTO.getAreaAtuacao());
        consulta.setTipoConsulta(consultaUpdateRequestDTO.getTipoConsulta());
        consulta.setProfissionalSaude(profissionalSaude);
        consulta.setPaciente(paciente);
        consulta.setStatusConsulta(StatusConsulta.N);

        return Consulta.toResponseDTO(consultaRepository.save(consulta));
    }

    public void delete(Long id) {
        consultaRepository.deleteById(id);
    }

    private void validarParametrosObrigatorios(ConsultaRequestDTO consultaRequestDTO) {
        if (consultaRequestDTO.getDataHoraConsulta() == null) {
            throw new ParametroInvalidoException("O campo Data e hora da consulta é obrigatório.");
        }
        if (consultaRequestDTO.getAreaAtuacao() == null) {
            throw new ParametroInvalidoException("O campo Área de atuação é obrigatório.");
        }
        if (consultaRequestDTO.getTipoConsulta() == null) {
            throw new ParametroInvalidoException("O campo Tipo da consulta é obrigatório.");
        }
        if (consultaRequestDTO.getNomePaciente() == null || consultaRequestDTO.getNomePaciente().isEmpty()) {
            throw new ParametroInvalidoException("O campo Nome do paciente é obrigatório.");
        }
        if (consultaRequestDTO.getCRM() == null || consultaRequestDTO.getCRM().isEmpty()) {
            throw new ParametroInvalidoException("O campo CRM do profissional de saúde é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(ConsultaUpdateRequestDTO consultaUpdateRequestDTO) {
        if (consultaUpdateRequestDTO.getDataHoraConsultaAntiga() == null) {
            throw new ParametroInvalidoException("O campo Data e hora da consulta antiga é obrigatório.");
        }
        if (consultaUpdateRequestDTO.getDataHoraNovaConsulta() == null) {
            throw new ParametroInvalidoException("O campo Data e hora da consulta nova é obrigatório.");
        }
        if (consultaUpdateRequestDTO.getAreaAtuacao() == null) {
            throw new ParametroInvalidoException("O campo Área de atuação é obrigatório.");
        }
        if (consultaUpdateRequestDTO.getTipoConsulta() == null) {
            throw new ParametroInvalidoException("O campo Tipo da consulta é obrigatório.");
        }
        if (consultaUpdateRequestDTO.getNomePaciente() == null || consultaUpdateRequestDTO.getNomePaciente().isEmpty()) {
            throw new ParametroInvalidoException("O campo Nome do paciente é obrigatório.");
        }
        if (consultaUpdateRequestDTO.getCRM() == null || consultaUpdateRequestDTO.getCRM().isEmpty()) {
            throw new ParametroInvalidoException("O campo CRM do profissional de saúde é obrigatório.");
        }
    }
}

