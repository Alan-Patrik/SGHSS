package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.events.ConsultaCriadaEvent;
import com.alanpatrik.sghss.api.events.ConsultaCriadaPayload;
import com.alanpatrik.sghss.api.exception.AcessoProibidoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Consulta;
import com.alanpatrik.sghss.api.model.dto.ConsultaDTO;
import com.alanpatrik.sghss.api.model.dto.request.ConsultaRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.ConsultaUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.enums.StatusConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.alanpatrik.sghss.api.repository.ConsultaRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final AgendaService agendaService;
    private final ProfissionalSaudeService profissionalSaudeService;
    private final UnidadeSaudeService unidadeSaudeService;
    private final PacienteService pacienteService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.base-url}")
    private String baseUrl;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public List<ConsultaDTO> getAll() {
        return consultaRepository.findAll().stream().map(Consulta::toDTO).toList();
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public ConsultaDTO findById(Long id) {
        var consulta = consultaRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.CONSULTA_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));
        return Consulta.toDTO(consulta);
    }

    @RequireRoles({Constantes.PRIV_ACESSAR_TELECONSULTA})
    @Transactional(readOnly = true)
    public String findByIdConsultaOnline(Long id, String token) {
        var consulta = consultaRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.CONSULTA_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        if (!consulta.getTipoConsulta().equals(TipoConsulta.O)) {
            throw new ParametroInvalidoException("A consulta não é online.");
        }
        if (consulta.getTokenUsuario() == null || !consulta.getTokenUsuario().equals(token)) {
            throw new AcessoProibidoException("Token inválido.");
        }
        if (consulta.getJoinTokenExpiresAt() == null && LocalDateTime.now().isAfter(consulta.getJoinTokenExpiresAt())) {
            throw new AcessoProibidoException("Link expirado.");
        }
        if (consulta.getMeetingUrl() == null || consulta.getMeetingUrl().isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Link não disponível.");
        }

        return consulta.getMeetingUrl();
    }

    @RequireRoles({Constantes.PRIV_AGENDAR_CONSULTA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ConsultaDTO save(ConsultaRequestDTO consultaRequestDTO) {
        this.validarParametrosObrigatorios(consultaRequestDTO);
        var profissionalSaude = profissionalSaudeService.findByCRM(consultaRequestDTO.getCRM());
        var paciente = pacienteService.findByName(consultaRequestDTO.getNomePaciente());
        var unidadeSaude = unidadeSaudeService.findByName(consultaRequestDTO.getNomeUnidadeSaude());

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
        if (unidadeSaude.getPacientes() != null) {
            for (var pa : unidadeSaude.getPacientes()) {
                if (!pa.getNome().equals(paciente.getNome())) {
                    unidadeSaude.getPacientes().add(paciente);
                }
            }
        }

        if (consulta.getTipoConsulta().equals(TipoConsulta.O)) {
            var token = java.util.UUID.randomUUID().toString().replace("-", "");
            var expiraEm = consulta.getDataHoraConsulta().plusMinutes(30);

            consulta.setTokenUsuario(token);
            consulta.setJoinTokenExpiresAt(expiraEm);
            String joinEndpoint = baseUrl + "/api/consultas/" + consulta.getId() + "/join/" + token;
            consulta.setMeetingUrl(joinEndpoint);
        }

        var payload = new ConsultaCriadaPayload(
                consulta.getPaciente().getEmail(),
                consulta.getPaciente().getNome(),
                consulta.getAreaAtuacao().getDescricao(),
                consulta.getDataHoraConsulta(),
                consulta.getTipoConsulta().getDescricao(),
                consulta.getMeetingUrl(),
                consulta.getJoinTokenExpiresAt(),
                Locale.forLanguageTag("pt-BR").toString()
        );

        // publica evento para envio de e-mail após commit
        eventPublisher.publishEvent(new ConsultaCriadaEvent(payload));

        return Consulta.toDTO(consulta);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ConsultaDTO update(Long id, ConsultaUpdateRequestDTO consultaUpdateRequestDTO) {
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
        consulta.setProfissionalSaude(profissionalSaude);
        consulta.setPaciente(paciente);
        consulta.setStatusConsulta(StatusConsulta.N);

        return Consulta.toDTO(consultaRepository.save(consulta));
    }

    @RequireRoles({Constantes.PRIV_CANCELAR_CONSULTA})
    @Transactional
    public String cancel(ConsultaRequestDTO consultaRequestDTO) {
        this.validarParametrosObrigatorios(consultaRequestDTO);

        var consulta = consultaRepository
                .findByDataHoraConsulta(consultaRequestDTO.getDataHoraConsulta())
                .orElseThrow(() -> new InformacaoNaoEncontradaException(
                        Constantes.CONSULTA_NOT_FOUND_BY_DATA_HORA_MESSAGE
                                .replace("%s", consultaRequestDTO.getDataHoraConsulta().toString())
                ));

        if (!consulta.getStatusConsulta().equals(StatusConsulta.N)) {
            throw new ParametroInvalidoException(Constantes.CONSULTA_BAD_REQUEST_MESSAGE);
        }

        agendaService.cancelTime(consulta.getProfissionalSaude().getAgenda().getId(), consultaRequestDTO.getDataHoraConsulta());

        consulta.setStatusConsulta(StatusConsulta.C);
        consultaRepository.save(consulta);

        return "Consulta cancelada com sucesso.";
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var consulta = consultaRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.CONSULTA_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        var paciente = consulta.getPaciente();
        if (paciente != null) {
            paciente.getConsultas().removeIf(p -> p.getId().equals(id));
            consulta.setPaciente(null);
        }

        var profissionalSaude = consulta.getProfissionalSaude();
        if (profissionalSaude != null) {
            profissionalSaude.getConsultas().removeIf(p -> p.getId().equals(id));
            consulta.setProfissionalSaude(null);
        }

        consultaRepository.delete(consulta);
        return "Consulta deletada com sucesso.";
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
        if (consultaUpdateRequestDTO.getNomePaciente() == null || consultaUpdateRequestDTO.getNomePaciente().isEmpty()) {
            throw new ParametroInvalidoException("O campo Nome do paciente é obrigatório.");
        }
        if (consultaUpdateRequestDTO.getCRM() == null || consultaUpdateRequestDTO.getCRM().isEmpty()) {
            throw new ParametroInvalidoException("O campo CRM do profissional de saúde é obrigatório.");
        }
    }
}

