package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.Leito;
import com.alanpatrik.sghss.api.model.ProfissionalSaude;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnidadeSaudeResponseDTO {

    private Long id;
    private String nome;
    private Endereco endereco;
    private List<Leito> leitos;
    private List<ProfissionalSaude> profissionais;
}
