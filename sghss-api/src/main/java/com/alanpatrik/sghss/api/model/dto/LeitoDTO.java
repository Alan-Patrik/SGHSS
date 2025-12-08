package com.alanpatrik.sghss.api.model.dto;

import com.alanpatrik.sghss.api.model.dto.response.PacienteResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.ProfissionalSaudeResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.UnidadeSaudeResponseDTO;
import lombok.*;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeitoDTO {

    private Long id;
    private String numero;
    private UnidadeSaudeResponseDTO unidadeSaude;
    private Set<PacienteResponseDTO> pacientes;
    private Set<ProfissionalSaudeResponseDTO> profissionaisSaude;
}
