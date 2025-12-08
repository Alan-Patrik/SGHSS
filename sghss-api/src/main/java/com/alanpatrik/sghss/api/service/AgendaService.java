package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Agenda;
import com.alanpatrik.sghss.api.model.dto.AgendaDTO;
import com.alanpatrik.sghss.api.model.dto.HorarioDisponivelDTO;
import com.alanpatrik.sghss.api.model.dto.request.AgendaRequestDTO;
import com.alanpatrik.sghss.api.model.enums.StatusHorario;
import com.alanpatrik.sghss.api.repository.AgendaRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final ProfissionalSaudeService profissionalSaudeService;

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional(readOnly = true)
    public List<AgendaDTO> getAll() {
        return agendaRepository.findAll().stream().map(Agenda::toResponseDTO).toList();
    }

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional(readOnly = true)
    public AgendaDTO findById(Long id) {
        if (id == null) {
            throw new ParametroInvalidoException("O campo Id da Agenda é obrigatório.");
        }

        var agenda = agendaRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.AGENDA_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));
        return Agenda.toResponseDTO(agenda);
    }

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AgendaDTO save(AgendaRequestDTO agendaRequestDTO) {
        this.validarParametrosObrigatorios(agendaRequestDTO);

        var profissionalSaude = profissionalSaudeService.findByCRM(agendaRequestDTO.getProfissionalSaude());
        if (profissionalSaude.getAgenda() != null) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        var agenda = Agenda.builder()
                .profissionalSaude(profissionalSaude)
                .horariosDisponiveis(new LinkedHashSet<>())
                .build();

        return Agenda.toResponseDTO(agendaRepository.save(agenda));
    }

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AgendaDTO addTime(Long id, LocalDateTime dataHoraNovaConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraNovaConsulta);
        var agendaResponseDTO = this.findById(id);

        var horariosDisponiveis = new ArrayList<HorarioDisponivelDTO>();
        for (var horario : agendaResponseDTO.getHorariosDisponiveis()) {
            if (horario.getHorarioDisponivel().equals(dataHoraNovaConsulta)) {
                throw new ConflitoException(
                        Constantes.AGENDA_CONFLICT_BY_HORARIO_MESSAGE
                                .replace("%s", dataHoraNovaConsulta.toString()
                                ));
            }
            horariosDisponiveis.add(horario);
        }

        if (agendaResponseDTO.getHorariosDisponiveis().isEmpty()) {
            var horarioDisponivelDTO = new HorarioDisponivelDTO();
            horarioDisponivelDTO.setStatus(StatusHorario.D);
            horarioDisponivelDTO.setHorarioDisponivel(dataHoraNovaConsulta);
            horariosDisponiveis.add(horarioDisponivelDTO);
        } else {
            var horarioDTO = HorarioDisponivelDTO.builder()
                    .status(StatusHorario.D)
                    .horarioDisponivel(dataHoraNovaConsulta)
                    .build();
            horariosDisponiveis.add(horarioDTO);
        }

        var listaOrdenada = horariosDisponiveis.stream()
                .sorted(Comparator.comparing(h -> h.getHorarioDisponivel().toLocalTime()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        agendaResponseDTO.setHorariosDisponiveis(listaOrdenada);

        var agenda = agendaRepository.save(Agenda.toEntity(agendaResponseDTO));
        return Agenda.toResponseDTO(agenda);
    }

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AgendaDTO updateTime(Long id, LocalDateTime dataHoraConsultaAntiga, LocalDateTime dataHoraNovaConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraConsultaAntiga, dataHoraNovaConsulta);
        var agendaResponseDTO = this.findById(id);

        var horariosDisponiveis = new ArrayList<HorarioDisponivelDTO>();
        var containsHorarioAntigo = false;
        var containsHorarioNovo = false;
        for (var horarioDTO : agendaResponseDTO.getHorariosDisponiveis()) {
            if (horarioDTO.getHorarioDisponivel().equals(dataHoraNovaConsulta)) {
                if (horarioDTO.getStatus() == StatusHorario.N) {
                    throw new InformacaoNaoEncontradaException(
                            Constantes.AGENDA_NOT_FOUND_BY_HORARIO_MESSAGE
                                    .replace("%s", dataHoraNovaConsulta.toString()
                                    ));
                }

                horarioDTO.setStatus(StatusHorario.N);
                horariosDisponiveis.add(horarioDTO);
                containsHorarioNovo = true;
            } else {
                horariosDisponiveis.add(horarioDTO);
            }
        }

        for (var horarioAntigoDTO : horariosDisponiveis) {
            if (horarioAntigoDTO.getHorarioDisponivel().equals(dataHoraConsultaAntiga)) {
                horariosDisponiveis.remove(horarioAntigoDTO);

                horarioAntigoDTO.setStatus(StatusHorario.D);
                horarioAntigoDTO.setHorarioDisponivel(dataHoraConsultaAntiga);
                horariosDisponiveis.add(horarioAntigoDTO);
                containsHorarioAntigo = true;
                break;
            }
        }

        if (!containsHorarioAntigo) {
            throw new InformacaoNaoEncontradaException(
                    Constantes.AGENDA_NOT_FOUND_BY_HORARIO_MESSAGE
                            .replace("%s", dataHoraConsultaAntiga.toString()));
        }

        if (!containsHorarioNovo) {
            throw new InformacaoNaoEncontradaException(
                    Constantes.AGENDA_NOT_FOUND_BY_HORARIO_DISPONIVEL_MESSAGE
                            .replace("%s", dataHoraNovaConsulta.toString()));
        }

        var listaOrdenada = horariosDisponiveis.stream()
                .sorted(Comparator.comparing(h -> h.getHorarioDisponivel().toLocalTime()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        agendaResponseDTO.setHorariosDisponiveis(listaOrdenada);

        var agenda = agendaRepository.save(Agenda.toEntity(agendaResponseDTO));
        return Agenda.toResponseDTO(agenda);
    }

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void schedule(Long id, LocalDateTime dataHoraConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraConsulta);
        var agenda = Agenda.toEntity(this.findById(id));

        var containsHorario = false;
        var horarioDTO = new HorarioDisponivelDTO();
        var horariosDisponiveis = new LinkedHashSet<HorarioDisponivelDTO>();
        if (agenda.getHorariosDisponiveis().isEmpty()) {
            throw new InformacaoNaoEncontradaException("A agenda do profissional de saúde não possui horários disponíveis.");
        }
        for (var horario : agenda.getHorariosDisponiveis()) {
            if (horario.getHorarioDisponivel().equals(dataHoraConsulta) &&
                    horario.getStatus().equals(StatusHorario.D)) {
                horariosDisponiveis.remove(horario);

                horarioDTO.setStatus(StatusHorario.N);
                horarioDTO.setHorarioDisponivel(dataHoraConsulta);
                horariosDisponiveis.add(horarioDTO);
                containsHorario = true;
            } else {
                horariosDisponiveis.add(horario);
            }
        }

        if (!containsHorario) {
            throw new InformacaoNaoEncontradaException("Horário não encontrado.");
        }

        var listaOrdenada = horariosDisponiveis.stream()
                .sorted(Comparator.comparing(h -> h.getHorarioDisponivel().toLocalTime()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        agenda.setHorariosDisponiveis(listaOrdenada);

        agendaRepository.save(agenda);
    }

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional
    public void cancelTime(Long id, LocalDateTime dataHoraConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraConsulta);
        var agendaResponseDTO = this.findById(id);

        var containsHorario = false;
        var horariosDisponiveis = new LinkedHashSet<HorarioDisponivelDTO>();
        for (var horarioDTO : agendaResponseDTO.getHorariosDisponiveis()) {
            if (horarioDTO.getHorarioDisponivel().equals(dataHoraConsulta) &&
                    horarioDTO.getStatus() == StatusHorario.N) {
                horariosDisponiveis.remove(horarioDTO);

                horarioDTO.setStatus(StatusHorario.D);
                horariosDisponiveis.add(horarioDTO);
                containsHorario = true;
            } else {
                horariosDisponiveis.add(horarioDTO);
            }
        }

        if (!containsHorario) {
            throw new InformacaoNaoEncontradaException(
                    Constantes.AGENDA_NOT_FOUND_BY_HORARIO_MESSAGE.replace("%s", dataHoraConsulta.toString()));
        }

        agendaResponseDTO.setHorariosDisponiveis(horariosDisponiveis);
        agendaRepository.save(Agenda.toEntity(agendaResponseDTO));
    }

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional
    public void deleteTime(Long id, LocalDateTime dataHoraConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraConsulta);
        var agendaResponseDTO = this.findById(id);

        var containsHorario = false;
        var horariosDisponiveis = new LinkedHashSet<HorarioDisponivelDTO>();
        for (var horarioDTO : agendaResponseDTO.getHorariosDisponiveis()) {
            if (horarioDTO.getHorarioDisponivel().equals(dataHoraConsulta) &&
                    horarioDTO.getStatus() == StatusHorario.D) {
                horariosDisponiveis.remove(horarioDTO);
                containsHorario = true;
            } else {
                horariosDisponiveis.add(horarioDTO);
            }
        }

        if (!containsHorario) {
            throw new InformacaoNaoEncontradaException(
                    Constantes.AGENDA_NOT_FOUND_BY_HORARIO_DISPONIVEL_MESSAGE
                            .replace("%s", dataHoraConsulta.toString()));
        }

        agendaResponseDTO.setHorariosDisponiveis(horariosDisponiveis);
        agendaRepository.save(Agenda.toEntity(agendaResponseDTO));
    }

    @RequireRoles({Constantes.PRIV_GERENCIAR_AGENDAS})
    @Transactional
    public String delete(Long id) {
        var agenda = agendaRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.AGENDA_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        var profissionalSaude = agenda.getProfissionalSaude();
        if (profissionalSaude != null) {
            profissionalSaude.setAgenda(null);
            agenda.setProfissionalSaude(null);
        }

        agendaRepository.save(agenda);
        return "Agenda deletada com sucesso.";
    }

    private void validarParametrosObrigatorios(Long id, LocalDateTime dataHoraConsulta) {
        if (id == null) {
            throw new ParametroInvalidoException("O campo Id da Agenda é obrigatório.");
        }

        if (dataHoraConsulta == null) {
            throw new ParametroInvalidoException("O campo Data e Hora da consulta é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(Long id, LocalDateTime dataHoraConsultaAntiga, LocalDateTime dataHoraConsultaNova) {
        if (id == null) {
            throw new ParametroInvalidoException("O campo Id da Agenda é obrigatório.");
        }

        if (dataHoraConsultaAntiga == null) {
            throw new ParametroInvalidoException("O campo Data e Hora da consulta antiga é obrigatório.");
        }

        if (dataHoraConsultaNova == null) {
            throw new ParametroInvalidoException("O campo Data e Hora da consulta nova é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(AgendaRequestDTO agendaRequestDTO) {
        if (agendaRequestDTO.getProfissionalSaude() == null) {
            throw new ParametroInvalidoException("O campo CRM do Profissional de Saúde é obrigatório.");
        }
    }
}