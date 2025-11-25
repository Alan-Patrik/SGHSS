package com.alanpatrik.sghss.api.model.dto.request;

import com.alanpatrik.sghss.api.model.Endereco;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnidadeSaudeRequestDTO {

    private String nome;
    private Endereco endereco;
}
