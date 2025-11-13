package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Agenda;
import com.alanpatrik.sghss.api.model.dto.HorarioDisponivelDTO;
import com.alanpatrik.sghss.api.model.dto.request.AgendaRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.AgendaResponseDTO;
import com.alanpatrik.sghss.api.model.enums.StatusHorario;
import com.alanpatrik.sghss.api.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final ProfissionalSaudeService profissionalSaudeService;

    public List<AgendaResponseDTO> getAll() {
        return agendaRepository.findAll().stream().map(Agenda::toResponseDTO).toList();
    }

    public AgendaResponseDTO findById(Long id) {
        if (id == null) {
            throw new ParametroInvalidoException("O campo Id da Agenda é obrigatório.");
        }

        var agenda = agendaRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return Agenda.toResponseDTO(agenda);
    }

    public AgendaResponseDTO save(AgendaRequestDTO agendaRequestDTO) {
        this.validarParametrosObrigatorios(agendaRequestDTO);

        var profissionalSaude = profissionalSaudeService.findByCRM(agendaRequestDTO.getProfissionalSaude());
        if (profissionalSaude.getAgenda() != null) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

//        var horariosDisponiveis = this.gerarHorariosDisponiveis();
//        var horarioDTOList = new ArrayList<HorarioDisponivelDTO>();
//        for (var horario : horariosDisponiveis) {
//            var horarioDTO = new HorarioDisponivelDTO();
//            horarioDTO.setStatus(StatusHorario.D);
//            horarioDTO.setHorarioDisponivel(horario);
//            horarioDTOList.add(horarioDTO);
//        }

        var agenda = Agenda.builder()
                .profissionalSaude(profissionalSaude)
                .horariosDisponiveis(new ArrayList<>())
                .build();

        return Agenda.toResponseDTO(agendaRepository.save(agenda));
    }

    public AgendaResponseDTO addTime(Long id, LocalDateTime dataHoraNovaConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraNovaConsulta);
        var agendaResponseDTO = this.findById(id);

        var horariosDisponiveis = new ArrayList<HorarioDisponivelDTO>();
        for (var horario : agendaResponseDTO.getHorariosDisponiveis()) {
            if (horario.getHorarioDisponivel().equals(dataHoraNovaConsulta)) {
                throw new ConflitoException("Horário informado já se encontra disponível.");
            }
            horariosDisponiveis.add(horario);
        }

        if (agendaResponseDTO.getHorariosDisponiveis() == null ||
                agendaResponseDTO.getHorariosDisponiveis().isEmpty()) {
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
                .toList();
        agendaResponseDTO.setHorariosDisponiveis(listaOrdenada);

        var agenda = agendaRepository.save(Agenda.toEntity(agendaResponseDTO));
        return Agenda.toResponseDTO(agenda);
    }

    public AgendaResponseDTO updateTime(Long id, LocalDateTime dataHoraConsultaAntiga, LocalDateTime dataHoraNovaConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraNovaConsulta);
        var agendaResponseDTO = this.findById(id);

        var horariosDisponiveis = new ArrayList<HorarioDisponivelDTO>();
        var containsHorarioAntigo = false;
        var containsHorarioNovo = false;
        for (var horarioDTO : agendaResponseDTO.getHorariosDisponiveis()) {
            if (horarioDTO.getHorarioDisponivel().equals(dataHoraNovaConsulta)) {
                if (horarioDTO.getStatus() == StatusHorario.N) {
                    throw new InformacaoNaoEncontradaException("Horário informado não disponível.");
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
            throw new InformacaoNaoEncontradaException("Horário não encontrado.");
        }

        if (!containsHorarioNovo) {
            throw new InformacaoNaoEncontradaException("Horário da nova consulta não encontrada ou indisponível.");
        }

        var listaOrdenada = horariosDisponiveis.stream()
                .sorted(Comparator.comparing(h -> h.getHorarioDisponivel().toLocalTime()))
                .toList();
        agendaResponseDTO.setHorariosDisponiveis(listaOrdenada);

        var agenda = agendaRepository.save(Agenda.toEntity(agendaResponseDTO));
        return Agenda.toResponseDTO(agenda);
    }

    public void schedule(Long id, LocalDateTime dataHoraConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraConsulta);
        var agendaResponseDTO = this.findById(id);

        var containsHorario = false;
        var horarioDTO = new HorarioDisponivelDTO();
        var horariosDisponiveis = new ArrayList<HorarioDisponivelDTO>();
        for (var horario : agendaResponseDTO.getHorariosDisponiveis()) {
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
                .toList();
        agendaResponseDTO.setHorariosDisponiveis(listaOrdenada);

        agendaRepository.save(Agenda.toEntity(agendaResponseDTO));
    }

    public void deleteTime(Long id, LocalDateTime dataHoraConsulta) {
        this.validarParametrosObrigatorios(id, dataHoraConsulta);
        var agendaResponseDTO = this.findById(id);

        var containsHorario = false;
        var horariosDisponiveis = new ArrayList<HorarioDisponivelDTO>();
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
            throw new InformacaoNaoEncontradaException("Horário informado não encontrado ou agendado.");
        }

        agendaResponseDTO.setHorariosDisponiveis(horariosDisponiveis);
        agendaRepository.save(Agenda.toEntity(agendaResponseDTO));
    }

    private void validarParametrosObrigatorios(Long id, LocalDateTime dataHoraConsulta) {
        if (id == null) {
            throw new ParametroInvalidoException("O campo Id da Agenda é obrigatório.");
        }

        if (dataHoraConsulta == null) {
            throw new ParametroInvalidoException("O campo Data e Hora da consulta é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(AgendaRequestDTO agendaRequestDTO) {
        if (agendaRequestDTO.getProfissionalSaude() == null) {
            throw new ParametroInvalidoException("O campo CRM do Profissional de Saúde é obrigatório.");
        }
    }

    private List<LocalDateTime> gerarHorariosDisponiveis() {
        List<LocalDateTime> horarios = new ArrayList<>();

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.plusDays(1);
        LocalDate fim = inicio
                .plusMonths(2)
                .withDayOfMonth(
                        inicio
                                .plusMonths(2)
                                .lengthOfMonth()
                );

        for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {
            DayOfWeek diaSemana = data.getDayOfWeek();
            if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
                // Horários padrão: 08:00, 08:30, 09:00, 09:30, 10:00, 10:30, 11:00
                // 11:30, 12:00, 13:00, 13:30, 14:00,14:30, 15:00, 15:30, 16:00, 16:30
                int[] horas = {8, 9, 10, 11, 12, 13, 14, 15, 16};
                for (int hora : horas) {
                    horarios.add(LocalDateTime.of(data, LocalTime.of(hora, 0)));
                    horarios.add(LocalDateTime.of(data, LocalTime.of(hora, 30)));
                }
            }
        }

        return horarios;
    }
}