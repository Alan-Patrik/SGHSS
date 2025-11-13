package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.HorarioDisponivelDTO;
import com.alanpatrik.sghss.api.model.dto.response.AgendaResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<HorarioDisponivelDTO> horariosDisponiveis;

    @OneToOne
    private ProfissionalSaude profissionalSaude;


    public static AgendaResponseDTO toResponseDTO(Agenda agenda) {
        var agendaResponseDTO = new AgendaResponseDTO();
        agendaResponseDTO.setId(agenda.getId());
        agendaResponseDTO.setProfissionalSaude(agenda.getProfissionalSaude());
        agendaResponseDTO.setHorariosDisponiveis(agenda.getHorariosDisponiveis());
        return agendaResponseDTO;
    }

    public static Agenda toEntity(AgendaResponseDTO agendaResponseDTO) {
        var agenda = new Agenda();
        agenda.setId(agendaResponseDTO.getId());
        agenda.setProfissionalSaude(agendaResponseDTO.getProfissionalSaude());
        agenda.setHorariosDisponiveis(agendaResponseDTO.getHorariosDisponiveis());
        return agenda;
    }

    public static List<AgendaResponseDTO> toResponseDTOList(List<Agenda> agendaList) {
        var agendas = new ArrayList<AgendaResponseDTO>();
        for (Agenda agenda : agendaList) {
            var AgendaResponseDTO = toResponseDTO(agenda);
            agendas.add(AgendaResponseDTO);
        }
        return agendas;
    }
}