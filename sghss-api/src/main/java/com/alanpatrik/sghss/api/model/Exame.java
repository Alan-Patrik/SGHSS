package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.ExameResponseDTO;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoExame;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "EXAME")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EXAME")
    private Long id;

    @Column(name = "TXT_OBSERVACAO", nullable = false)
    private String observacao;

    @Column(name = "TXT_TIPO_EXAME", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoExame tipoExame;

    @Column(name = "TXT_TIPO_CONSULTA", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConsulta tipoConsulta;

    @Column(name = "DATA_HORA_REALIZACAO", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataHoraExame;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "ID_PROFISSIONAL_SAUDE")
    private ProfissionalSaude profissionalSaude;

    @ManyToOne
    @JoinColumn(name = "ID_UNIDADE_SERVIÇO")
    private UnidadeSaude unidadeSaude;

    public static ExameResponseDTO toResponseDTO(Exame exame) {
        var exameResponseDTO = new ExameResponseDTO();
        exameResponseDTO.setId(exame.getId());
        exameResponseDTO.setObservacao(exame.getObservacao());
        exameResponseDTO.setUnidadeSaude(exame.getUnidadeSaude());
        exameResponseDTO.setTipoExame(exame.getTipoExame());
        exameResponseDTO.setTipoConsulta(exame.getTipoConsulta());
        exameResponseDTO.setDataHoraExame(exame.getDataHoraExame());
        exameResponseDTO.setPaciente(exame.getPaciente());
        exameResponseDTO.setProfissionalSaude(exame.getProfissionalSaude());
        return exameResponseDTO;
    }

    public static Exame toEntity(ExameResponseDTO exameResponseDTO) {
        var exame = new Exame();
        exame.setId(exameResponseDTO.getId());
        exame.setObservacao(exameResponseDTO.getObservacao());
        exame.setUnidadeSaude(exameResponseDTO.getUnidadeSaude());
        exame.setTipoExame(exameResponseDTO.getTipoExame());
        exame.setTipoConsulta(exameResponseDTO.getTipoConsulta());
        exame.setDataHoraExame(exameResponseDTO.getDataHoraExame());
        exame.setPaciente(exameResponseDTO.getPaciente());
        exame.setProfissionalSaude(exameResponseDTO.getProfissionalSaude());
        return exame;
    }
}
