package com.alanpatrik.sghss.api.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnidadeSaudeAdicionarProfissionalRequestDTO {
    private String nomeUnidadeSaude;
    private String crm;
}
