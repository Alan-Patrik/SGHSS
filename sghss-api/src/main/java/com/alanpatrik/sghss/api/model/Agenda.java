package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.AgendaDTO;
import com.alanpatrik.sghss.api.model.dto.HorarioDisponivelDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AGENDA")
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AGENDA", nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "HORARIOS_DISPONIVEIS", joinColumns = @JoinColumn(name = "AGENDA_ID"))
    @Column(name = "HORARIO")
    private Set<HorarioDisponivelDTO> horariosDisponiveis = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    private ProfissionalSaude profissionalSaude;


    public static AgendaDTO toResponseDTO(Agenda agenda) {
        var agendaResponseDTO = new AgendaDTO();
        agendaResponseDTO.setId(agenda.getId());
        agendaResponseDTO.setProfissionalSaude(ProfissionalSaude.toResponseDTO(agenda.getProfissionalSaude()));
        agendaResponseDTO.setHorariosDisponiveis(agenda.getHorariosDisponiveis());

        return agendaResponseDTO;
    }

    public static Agenda toEntity(AgendaDTO agendaResponseDTO) {
        var agenda = new Agenda();
        agenda.setId(agendaResponseDTO.getId());
        agenda.setProfissionalSaude(ProfissionalSaude.toEntity(agendaResponseDTO.getProfissionalSaude()));
        agenda.setHorariosDisponiveis(agendaResponseDTO.getHorariosDisponiveis());

        return agenda;
    }
}