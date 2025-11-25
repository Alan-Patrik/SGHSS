package com.alanpatrik.sghss.api.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrescricaoRequestDTO {

    private String medicamento;
    private String observacao;
    private String dosagem;
    private String duracao;
    private Long idProntuario;

}
