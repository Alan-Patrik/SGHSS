package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.ExameDTO;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoExame;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @OneToOne(mappedBy = "exame", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Pagamento pagamento;

    public static ExameDTO toResponseDTO(Exame exame) {
        var exameDTO = new ExameDTO();
        exameDTO.setId(exame.getId());
        exameDTO.setObservacao(exame.getObservacao());
        exameDTO.setUnidadeSaude(UnidadeSaude.toResponseDTO(exame.getUnidadeSaude()));
        exameDTO.setTipoExame(exame.getTipoExame());
        exameDTO.setTipoConsulta(exame.getTipoConsulta());
        exameDTO.setDataHoraExame(exame.getDataHoraExame());
        exameDTO.setPaciente(Paciente.toResponseDTO(exame.getPaciente()));
        exameDTO.setProfissionalSaude(ProfissionalSaude.toResponseDTO(exame.getProfissionalSaude()));
        return exameDTO;
    }

    public static Exame toEntity(ExameDTO exameDTO) {
        var exame = new Exame();
        exame.setId(exameDTO.getId());
        exame.setObservacao(exameDTO.getObservacao());
        exame.setUnidadeSaude(UnidadeSaude.toEntity(exameDTO.getUnidadeSaude()));
        exame.setTipoExame(exameDTO.getTipoExame());
        exame.setTipoConsulta(exameDTO.getTipoConsulta());
        exame.setDataHoraExame(exameDTO.getDataHoraExame());
        exame.setPaciente(Paciente.toEntity(exameDTO.getPaciente()));
        exame.setProfissionalSaude(ProfissionalSaude.toEntity(exameDTO.getProfissionalSaude()));
        return exame;
    }

    public static Set<ExameDTO> toResponseDTOList(Set<Exame> exames) {
        var exameDTOList = new LinkedHashSet<ExameDTO>();
        for (Exame exame : exames) {
            exameDTOList.add(toResponseDTO(exame));
        }
        return exameDTOList;
    }

    public void vincularPagamento(Pagamento pagamento) {
        if (pagamento != null) {
            pagamento.setExame(this);
            pagamento.setConsulta(null);
            this.pagamento = pagamento;
        }
    }
}
