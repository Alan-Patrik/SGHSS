package com.alanpatrik.sghss.api.model.dto;

import com.alanpatrik.sghss.api.model.dto.response.PacienteResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.ProfissionalSaudeResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.UnidadeSaudeResponseDTO;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoExame;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExameDTO {

    private Long id;
    private String observacao;
    private TipoExame tipoExame;
    private TipoConsulta tipoConsulta;
    private LocalDateTime dataHoraExame;
    private PacienteResponseDTO paciente;
    private UnidadeSaudeResponseDTO unidadeSaude;
    private ProfissionalSaudeResponseDTO profissionalSaude;
}
