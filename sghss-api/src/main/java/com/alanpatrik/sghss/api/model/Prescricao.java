package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.dto.response.PrescricaoResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "PRESCRICAO")
public class Prescricao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRESCRICAO", nullable = false)
    private Long id;

    @Column(name = "TXT_NOME_MEDICAMENTO", nullable = false)
    private String medicamento;

    @Column(name = "TXT_OBSERVACAO")
    private String observacao;

    @Column(name = "TXT_DOSAGEM", nullable = false)
    private String dosagem;

    @Column(name = "TXT_DURACAO_MEDICACAO", nullable = false)
    private String duracao;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_PRONTUARIO")
    private Prontuario prontuario;

    public static PrescricaoResponseDTO toResponseDTO(Prescricao prescricao) {
        var prescricaoDTO = new PrescricaoResponseDTO();
        prescricaoDTO.setId(prescricao.getId());
        prescricaoDTO.setMedicamento(prescricao.getMedicamento());
        prescricaoDTO.setObservacao(prescricao.getObservacao());
        prescricaoDTO.setDosagem(prescricao.getDosagem());
        prescricaoDTO.setDuracao(prescricao.getDuracao());
        return prescricaoDTO;
    }

    public static Prescricao toEntity(PrescricaoResponseDTO prescricaoResponseDTO) {
        var prescricao = new Prescricao();
        prescricao.setId(prescricaoResponseDTO.getId());
        prescricao.setMedicamento(prescricaoResponseDTO.getMedicamento());
        prescricao.setObservacao(prescricaoResponseDTO.getObservacao());
        prescricao.setDosagem(prescricaoResponseDTO.getDosagem());
        prescricao.setDuracao(prescricaoResponseDTO.getDuracao());
        return prescricao;
    }

    public static List<Prescricao> responseToEntityList(List<PrescricaoResponseDTO> prescricaoDTOList) {
        var prescricoes = new ArrayList<Prescricao>();
        for (PrescricaoResponseDTO prescricaoDTO : prescricaoDTOList) {
            var prescricao = toEntity(prescricaoDTO);

            prescricoes.add(prescricao);
        }
        return prescricoes;
    }
}
