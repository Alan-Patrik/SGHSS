package com.alanpatrik.sghss.api.model.dto;

import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.dto.response.LeitoResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.PacienteResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.ProfissionalSaudeResponseDTO;
import lombok.*;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnidadeSaudeDTO {

    private Long id;
    private String nome;
    private Endereco endereco;
    private Set<LeitoResponseDTO> leitos;
    private Set<ProfissionalSaudeResponseDTO> profissionais;
    private Set<PacienteResponseDTO> pacientes;
}
