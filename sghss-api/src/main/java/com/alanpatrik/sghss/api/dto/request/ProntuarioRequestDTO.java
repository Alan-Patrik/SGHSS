package com.alanpatrik.sghss.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProntuarioRequestDTO {
    private String observacao;
    private String nomePaciente;

}
