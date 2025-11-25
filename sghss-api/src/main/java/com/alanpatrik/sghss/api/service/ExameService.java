package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Exame;
import com.alanpatrik.sghss.api.model.dto.request.ExameRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.ExameUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.ExameResponseDTO;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoExame;
import com.alanpatrik.sghss.api.repository.ExameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExameService {

    private final ExameRepository exameRepository;
    private final UnidadeSaudeService unidadeSaudeService;
    private final PacienteService pacienteService;
    private final ProfissionalSaudeService profissionalSaudeService;
    private final AgendaService agendaService;

    public List<ExameResponseDTO> getAll() {
        return exameRepository.findAll().stream().map(Exame::toResponseDTO).collect(Collectors.toList());
    }

    public ExameResponseDTO findById(Long id) {
        var exame = exameRepository.findById(id)
                .orElseThrow(() -> new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return Exame.toResponseDTO(exame);
    }

    @Transactional
    public ExameResponseDTO save(ExameRequestDTO exameRequestDTO) {
        this.validarParametrosObrigatorios(exameRequestDTO);

        var unidadeSaude = unidadeSaudeService.findByName(exameRequestDTO.getNomeUnidadeSaude());
        var profissionalSaude = profissionalSaudeService.findByCRM(exameRequestDTO.getCRM());
        var paciente = pacienteService.findByName(exameRequestDTO.getNomePaciente());
        var examesPaciente = exameRepository.findByPaciente(paciente);

        var maiorQue90Dias = isMaiorQue90Dias(exameRequestDTO, examesPaciente);
        if (maiorQue90Dias) {
            throw new ParametroInvalidoException("O Paciente já possui o exame informado.");
        }

        agendaService.schedule(profissionalSaude.getAgenda().getId(), exameRequestDTO.getDataHoraExame());

        var exame = Exame.builder()
                .unidadeSaude(unidadeSaude)
                .paciente(paciente)
                .profissionalSaude(profissionalSaude)
                .observacao(exameRequestDTO.getObservacao())
                .dataHoraExame(exameRequestDTO.getDataHoraExame())
                .tipoExame(TipoExame.getEnum(exameRequestDTO.getTipoExame().getCodigo()))
                .tipoConsulta(TipoConsulta.getEnum(exameRequestDTO.getTipoConsulta().getCodigo()))
                .build();

        exame = exameRepository.save(exame);
        return Exame.toResponseDTO(exame);
    }

    @Transactional
    public ExameResponseDTO update(Long id, ExameUpdateRequestDTO exameUpdateRequestDTO) {
        this.validarParametrosObrigatorios(exameUpdateRequestDTO);
        var exame = Exame.toEntity(this.findById(id));
        var unidadeSaude = unidadeSaudeService.findByName(exameUpdateRequestDTO.getNomeUnidadeSaude());
        var profissionalSaude = profissionalSaudeService.findByCRM(exameUpdateRequestDTO.getCRM());
        var paciente = pacienteService.findByName(exameUpdateRequestDTO.getNomePaciente());
        var examesPaciente = exameRepository.findByPaciente(paciente);

        var maiorQue90Dias = isMaiorQue90Dias(exameUpdateRequestDTO, examesPaciente);
        if (maiorQue90Dias) {
            throw new ParametroInvalidoException("O Paciente já possui o exame informado.");
        }

        agendaService.updateTime(id, exameUpdateRequestDTO.getDataHoraExameAntigo(), exameUpdateRequestDTO.getDataHoraExameNovo());

        exame.setUnidadeSaude(unidadeSaude);
        exame.setTipoConsulta(exameUpdateRequestDTO.getTipoConsulta());
        exame.setTipoExame(exameUpdateRequestDTO.getTipoExame());
        exame.setDataHoraExame(exameUpdateRequestDTO.getDataHoraExameNovo());
        exame.setPaciente(paciente);
        exame.setObservacao(exameUpdateRequestDTO.getObservacao());
        exame.setProfissionalSaude(profissionalSaude);

        exame = exameRepository.save(exame);
        return Exame.toResponseDTO(exame);
    }

    public void delete(Long id) {
        var exame = this.findById(id);
        exameRepository.deleteById(exame.getId());
    }

    private static boolean isMaiorQue90Dias(ExameRequestDTO exameRequestDTO, List<Exame> examesPaciente) {
        var maiorQue90Dias = false;
        if (!examesPaciente.isEmpty()) {
            for (var examePaciente : examesPaciente) {
                if (examePaciente.getPaciente().getNome().equals(exameRequestDTO.getNomePaciente()) &&
                        examePaciente.getTipoExame().equals(exameRequestDTO.getTipoExame())) {
                    maiorQue90Dias = exameRequestDTO.getDataHoraExame()
                            .isBefore(examePaciente.getDataHoraExame().plusDays(90));
                }
            }
        }
        return maiorQue90Dias;
    }

    private static boolean isMaiorQue90Dias(ExameUpdateRequestDTO exameUpdateRequestDTO, List<Exame> examesPaciente) {
        var maiorQue90Dias = false;
        if (!examesPaciente.isEmpty()) {
            for (var examePaciente : examesPaciente) {
                if (examePaciente.getPaciente().getNome().equals(exameUpdateRequestDTO.getNomePaciente()) &&
                        examePaciente.getTipoExame().equals(exameUpdateRequestDTO.getTipoExame())) {
                    maiorQue90Dias = exameUpdateRequestDTO.getDataHoraExameNovo()
                            .isBefore(examePaciente.getDataHoraExame().plusDays(90));
                }
            }
        }
        return maiorQue90Dias;
    }

    private void validarParametrosObrigatorios(ExameRequestDTO exameRequestDTO) {
        if (exameRequestDTO.getObservacao() == null || exameRequestDTO.getObservacao().isEmpty() || exameRequestDTO.getObservacao().isBlank()) {
            throw new ParametroInvalidoException("O campo Observação é obrigatório.");
        }
        if (exameRequestDTO.getNomeUnidadeSaude() == null || exameRequestDTO.getNomeUnidadeSaude().isEmpty() || exameRequestDTO.getNomeUnidadeSaude().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome da Unidade de Saúde é obrigatório.");
        }
        if (exameRequestDTO.getNomePaciente() == null || exameRequestDTO.getNomePaciente().isEmpty() || exameRequestDTO.getNomePaciente().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome do Paciente é obrigatório.");
        }
        if (exameRequestDTO.getCRM() == null || exameRequestDTO.getCRM().isEmpty() || exameRequestDTO.getCRM().isBlank()) {
            throw new ParametroInvalidoException("O campo CRV do profissional de saúde é obrigatório.");
        }
        if (exameRequestDTO.getTipoExame() == null) {
            throw new ParametroInvalidoException("O campo Tipo de exame é obrigatório.");
        }
        if (exameRequestDTO.getTipoConsulta() == null) {
            throw new ParametroInvalidoException("O campo Tipo de consulta é obrigatório.");
        }
        if (exameRequestDTO.getDataHoraExame() == null) {
            throw new ParametroInvalidoException("O campo Data/Hora do exame é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(ExameUpdateRequestDTO exameUpdateRequestDTO) {
        if (exameUpdateRequestDTO.getObservacao() == null || exameUpdateRequestDTO.getObservacao().isEmpty() || exameUpdateRequestDTO.getObservacao().isBlank()) {
            throw new ParametroInvalidoException("O campo Observação é obrigatório.");
        }
        if (exameUpdateRequestDTO.getNomeUnidadeSaude() == null || exameUpdateRequestDTO.getNomeUnidadeSaude().isEmpty() || exameUpdateRequestDTO.getNomeUnidadeSaude().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome da Unidade de Saúde é obrigatório.");
        }
        if (exameUpdateRequestDTO.getNomePaciente() == null || exameUpdateRequestDTO.getNomePaciente().isEmpty() || exameUpdateRequestDTO.getNomePaciente().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome do Paciente é obrigatório.");
        }
        if (exameUpdateRequestDTO.getCRM() == null || exameUpdateRequestDTO.getCRM().isEmpty() || exameUpdateRequestDTO.getCRM().isBlank()) {
            throw new ParametroInvalidoException("O campo CRV do profissional de saúde é obrigatório.");
        }
        if (exameUpdateRequestDTO.getTipoExame() == null) {
            throw new ParametroInvalidoException("O campo Tipo de exame é obrigatório.");
        }
        if (exameUpdateRequestDTO.getTipoConsulta() == null) {
            throw new ParametroInvalidoException("O campo Tipo de consulta é obrigatório.");
        }
        if (exameUpdateRequestDTO.getDataHoraExameAntigo() == null) {
            throw new ParametroInvalidoException("O campo Data/Hora do exame antigo é obrigatório.");
        }
        if (exameUpdateRequestDTO.getDataHoraExameNovo() == null) {
            throw new ParametroInvalidoException("O campo Data/Hora do novo exame é obrigatório.");
        }
    }
}

