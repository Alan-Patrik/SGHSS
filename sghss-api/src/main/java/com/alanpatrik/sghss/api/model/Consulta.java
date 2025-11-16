package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.ConsultaResponseDTO;
import com.alanpatrik.sghss.api.model.enums.AreaAtuacao;
import com.alanpatrik.sghss.api.model.enums.StatusConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CONSULTA")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONSULTA", nullable = false)
    private Long id;

    @Column(name = "DATA_HORA_CONSULTA", nullable = false)
    private LocalDateTime dataHoraConsulta;

    @Column(name = "AREA_ATUACAO", nullable = false)
    private AreaAtuacao areaAtuacao;

    @Column(name = "INDI_TXT_TIPO_CONSULTA", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConsulta tipoConsulta;

    @Column(name = "INDI_TXT_STATUS_CONSULTA", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusConsulta statusConsulta;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "ID_PROFISSIONAL_SAUDE")
    private ProfissionalSaude profissionalSaude;

    public static Consulta toEntity(ConsultaResponseDTO consultaResponseDTO) {
        var consulta = new Consulta();
        consulta.setId(consultaResponseDTO.getId());
        consulta.setDataHoraConsulta(consultaResponseDTO.getDataHoraConsulta());
        consulta.setAreaAtuacao(consultaResponseDTO.getAreaAtuacao());
        consulta.setTipoConsulta(consultaResponseDTO.getTipoConsulta());
        consulta.setStatusConsulta(consultaResponseDTO.getStatusConsulta());
        return consulta;
    }

    public static ConsultaResponseDTO toResponseDTO(Consulta consulta) {
        var consultaResponseDTO = new ConsultaResponseDTO();
        consultaResponseDTO.setId(consulta.getId());
        consultaResponseDTO.setDataHoraConsulta(consulta.getDataHoraConsulta());
        consultaResponseDTO.setAreaAtuacao(consulta.getAreaAtuacao());
        consultaResponseDTO.setStatusConsulta(consulta.getStatusConsulta());
        consultaResponseDTO.setTipoConsulta(consulta.getTipoConsulta());
        consultaResponseDTO.setCRM(consulta.profissionalSaude.getCRM());
        consultaResponseDTO.setNomePaciente(consulta.getPaciente().getNome());
        return consultaResponseDTO;
    }

    public static List<ConsultaResponseDTO> responseToDTOList(List<Consulta> Consultas) {
        var consultaResponseDTOList = new ArrayList<ConsultaResponseDTO>();
        for (var consulta : Consultas) {
            var consultaResponseDTO = toResponseDTO(consulta);
            consultaResponseDTOList.add(consultaResponseDTO);
        }
        return consultaResponseDTOList;
    }
}
