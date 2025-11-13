package com.alanpatrik.sghss.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrescricaoResponseDTO {
    private Long id;
    private String medicamento;
    private String observacao;
    private String dosagem;
    private String duracao;

}
