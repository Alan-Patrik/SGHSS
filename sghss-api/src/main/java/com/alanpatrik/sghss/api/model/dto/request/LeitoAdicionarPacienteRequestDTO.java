package com.alanpatrik.sghss.api.model.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeitoAdicionarPacienteRequestDTO {

    private String numeroLeito;
    private String nomePaciente;
}
