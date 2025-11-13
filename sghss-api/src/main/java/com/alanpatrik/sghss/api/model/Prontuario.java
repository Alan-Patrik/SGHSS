package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.ProntuarioResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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


    @Column(name = "TXT_OBSERVACAO")
    private String observacao;

    @Column(name = "DATA_CRIACAO", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataCriacao;

    @Column(name = "DATA_MODIFICACAO", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataModificacao;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "ID_PACIENTE")
    private Paciente paciente;

    @OneToMany(mappedBy = "prontuario", fetch = FetchType.LAZY)
    private List<Prescricao> prescricoes;

    public static ProntuarioResponseDTO toResponseDTO(Prontuario prontuario) {
        var prontuarioResponseDTO = new ProntuarioResponseDTO();
        prontuarioResponseDTO.setId(prontuario.getId());
        prontuarioResponseDTO.setDataCriacao(prontuario.getDataCriacao());
        prontuarioResponseDTO.setDataModificacao(prontuario.getDataModificacao());
        prontuarioResponseDTO.setObservacao(prontuario.getObservacao());
        prontuarioResponseDTO.setNomePaciente(prontuario.getPaciente().getNome());
        prontuarioResponseDTO.setPrescricoes(prontuario.getPrescricoes()
                .stream()
                .map(Prescricao::toResponseDTO)
                .toList());

        return prontuarioResponseDTO;
    }

    public static Prontuario toEntity(ProntuarioResponseDTO prontuarioDTO) {
        var prontuario = new Prontuario();
        prontuario.setId(prontuarioDTO.getId());
        prontuario.setDataCriacao(prontuarioDTO.getDataCriacao());
        prontuario.setDataModificacao(prontuarioDTO.getDataModificacao());
        prontuario.setObservacao(prontuarioDTO.getObservacao());
        prontuario.setPrescricoes(Prescricao.responseToEntityList(prontuarioDTO.getPrescricoes()));

        return prontuario;
    }
}
