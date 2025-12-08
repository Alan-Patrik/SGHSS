package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.ConsultaDTO;
import com.alanpatrik.sghss.api.model.enums.AreaAtuacao;
import com.alanpatrik.sghss.api.model.enums.StatusConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @Column(name = "TXT_TOKEN_USUARIO")
    private String tokenUsuario;

    @Column(name = "TXT_TOKEN_EXPIRES_AT")
    private LocalDateTime joinTokenExpiresAt;

    @Column(name = "TXT_MEETING_URL")
    private String meetingUrl;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROFISSIONAL_SAUDE")
    private ProfissionalSaude profissionalSaude;

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Pagamento pagamento;

    public static ConsultaDTO toDTO(Consulta consulta) {
        var consultaDTO = new ConsultaDTO();
        consultaDTO.setId(consulta.getId());
        consultaDTO.setDataHoraConsulta(consulta.getDataHoraConsulta());
        consultaDTO.setAreaAtuacao(consulta.getAreaAtuacao());
        consultaDTO.setStatusConsulta(consulta.getStatusConsulta());
        consultaDTO.setTipoConsulta(consulta.getTipoConsulta());
        consultaDTO.setCRM(consulta.getProfissionalSaude().getCRM());
        consultaDTO.setNomePaciente(consulta.getPaciente().getNome());
        consultaDTO.setToken(consulta.getTokenUsuario());
        consultaDTO.setJoinTokenExpiresAt(consulta.getJoinTokenExpiresAt());
        consultaDTO.setMeetingURL(consulta.getMeetingUrl());

        return consultaDTO;
    }

    public static Consulta toEntity(ConsultaDTO consultaDTO) {
        var consulta = new Consulta();
        consulta.setId(consultaDTO.getId());
        consulta.setDataHoraConsulta(consultaDTO.getDataHoraConsulta());
        consulta.setAreaAtuacao(consultaDTO.getAreaAtuacao());
        consulta.setTipoConsulta(consultaDTO.getTipoConsulta());
        consulta.setStatusConsulta(consultaDTO.getStatusConsulta());

        return consulta;
    }

    public static Set<ConsultaDTO> toDTOList(Set<Consulta> Consultas) {
        var consultaDTOList = new LinkedHashSet<ConsultaDTO>();
        for (var consulta : Consultas) {
            var consultaDTO = toDTO(consulta);
            consultaDTOList.add(consultaDTO);
        }
        return consultaDTOList;
    }

    public void vincularPagamento(Pagamento pagamento) {
        if (pagamento != null) {
            pagamento.setConsulta(this);
            pagamento.setExame(null);
            this.pagamento = pagamento;
        }
    }
}
