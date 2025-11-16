package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.UnidadeSaude;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeitoResponseDTO {
    private Long id;
    private String numero;
    private UnidadeSaude unidadeSaude;
    private List<PacienteResponseDTO> pacientes;
    private List<ProfissionalSaudeResponseDTO> profissionaisSaude;
}
