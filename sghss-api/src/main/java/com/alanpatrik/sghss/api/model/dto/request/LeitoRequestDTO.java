package com.alanpatrik.sghss.api.model.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeitoRequestDTO {

    private String numero;
    private String nomeUnidadeSaude;
}
