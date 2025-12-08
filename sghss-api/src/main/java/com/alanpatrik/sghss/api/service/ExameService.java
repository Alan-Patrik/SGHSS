package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Exame;
import com.alanpatrik.sghss.api.model.dto.ExameDTO;
import com.alanpatrik.sghss.api.model.dto.request.ExameRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.ExameUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoExame;
import com.alanpatrik.sghss.api.repository.ExameRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public List<ExameDTO> findAll() {
        return exameRepository.findAll().stream().map(Exame::toResponseDTO).collect(Collectors.toList());
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public ExameDTO findById(Long id) {
        var exame = exameRepository.findById(id)
                .orElseThrow(() -> new InformacaoNaoEncontradaException(
                        Constantes.EXAME_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id)))
                );
        return Exame.toResponseDTO(exame);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ExameDTO save(ExameRequestDTO exameRequestDTO) {
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

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ExameDTO update(Long id, ExameUpdateRequestDTO exameUpdateRequestDTO) {
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

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var exame = exameRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.EXAME_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id)))
        );

        var unidadeSaude = exame.getUnidadeSaude();
        if (unidadeSaude != null) {
            exame.setUnidadeSaude(unidadeSaude);
        }

        var profissionalSaude = exame.getProfissionalSaude();
        if (profissionalSaude != null) {
            exame.setProfissionalSaude(null);
        }

        var paciente = exame.getPaciente();
        if (paciente.getExames() != null && !paciente.getExames().isEmpty()) {
            for (var consulta : new ArrayList<>(paciente.getConsultas())) {
                consulta.setPaciente(null);
            }
            paciente.getExames().clear();
        }

        exameRepository.deleteById(exame.getId());

        return "Exame deletado com sucesso!";
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

