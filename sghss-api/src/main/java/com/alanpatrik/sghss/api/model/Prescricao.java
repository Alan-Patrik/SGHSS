package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.dto.PrescricaoDTO;
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

    @Column(name = "TXT_OBSERVACAO", nullable = false)
    private String observacao;

    @Column(name = "TXT_DOSAGEM", nullable = false)
    private String dosagem;

    @Column(name = "TXT_DURACAO_MEDICACAO", nullable = false)
    private String duracao;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_PRONTUARIO")
    private Prontuario prontuario;

    public static PrescricaoDTO toDTO(Prescricao prescricao) {
        var prescricaoDTO = new PrescricaoDTO();
        prescricaoDTO.setId(prescricao.getId());
        prescricaoDTO.setMedicamento(prescricao.getMedicamento());
        prescricaoDTO.setObservacao(prescricao.getObservacao());
        prescricaoDTO.setDosagem(prescricao.getDosagem());
        prescricaoDTO.setDuracao(prescricao.getDuracao());
        prescricaoDTO.setIdProntuario(prescricao.getProntuario().getId());
        return prescricaoDTO;
    }

    public static List<Prescricao> toEntityDTOLis(List<PrescricaoDTO> prescricaoDTOList) {
        var prescricoes = new ArrayList<Prescricao>();
        for (PrescricaoDTO prescricaoDTO : prescricaoDTOList) {
            var prescricao = new Prescricao();
            prescricao.setId(prescricaoDTO.getId());
            prescricao.setMedicamento(prescricaoDTO.getMedicamento());
            prescricao.setObservacao(prescricaoDTO.getObservacao());
            prescricao.setDosagem(prescricaoDTO.getDosagem());
            prescricao.setDuracao(prescricaoDTO.getDuracao());

            prescricoes.add(prescricao);
        }
        return prescricoes;
    }
}
