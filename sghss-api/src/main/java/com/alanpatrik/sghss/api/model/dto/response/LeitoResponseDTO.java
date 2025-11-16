package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.UnidadeSaude;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeitoResponseDTO {
    private Long id;
    private String numero;
    private UnidadeSaude unidadeSaude;
}
