package com.alanpatrik.sghss.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProntuarioResponseDTO {

    private Long id;
    private String observacao;
    private String nomePaciente;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataModificacao;
    private Set<PrescricaoResponseDTO> prescricoes;
}
