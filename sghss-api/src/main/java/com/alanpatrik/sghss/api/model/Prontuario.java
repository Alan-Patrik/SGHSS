package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.dto.ProntuarioDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "PRONTUARIO")
public class Prontuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRONTUARIO", nullable = false)
    private Long id;

    @Column(name = "DATA_PRONTUARIO", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataModificacao;

    @Column(name = "TXT_OBSERVACAO")
    private String observacao;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_PACIENTE")
    private Paciente paciente;

    @OneToMany(mappedBy = "prontuario", fetch = FetchType.LAZY)
    private List<Prescricao> prescricoes;

    public static ProntuarioDTO toDTO(Prontuario prontuario) {
        var prontuarioDTO = new ProntuarioDTO();
        prontuarioDTO.setId(prontuario.getId());
        prontuarioDTO.setDataCriacao(prontuario.getDataModificacao());
        prontuarioDTO.setDataModificacao(prontuario.getDataModificacao());
        prontuarioDTO.setObservacao(prontuario.getObservacao());
        prontuarioDTO.setNomePaciente(prontuario.getPaciente().getNome());
        prontuarioDTO.setPrescricoes(prontuario.getPrescricoes()
                .stream()
                .map(Prescricao::toDTO)
                .toList());

        return prontuarioDTO;
    }

    public static Prontuario toEntity(ProntuarioDTO prontuarioDTO) {
        var prontuario = new Prontuario();
        prontuario.setId(prontuarioDTO.getId());
        prontuario.setDataModificacao(prontuarioDTO.getDataModificacao());
        prontuario.setObservacao(prontuarioDTO.getObservacao());
        prontuario.setPrescricoes(Prescricao.toEntityDTOLis(prontuarioDTO.getPrescricoes()));

        return prontuario;
    }
}
