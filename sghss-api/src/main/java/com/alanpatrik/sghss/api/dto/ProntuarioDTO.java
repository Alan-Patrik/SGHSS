package com.alanpatrik.sghss.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProntuarioDTO {
    private Long id;
    private LocalDate dataModificacao;
    private String observacao;
    private String nomePaciente;
    private List<PrescricaoDTO> prescricoes;

}
