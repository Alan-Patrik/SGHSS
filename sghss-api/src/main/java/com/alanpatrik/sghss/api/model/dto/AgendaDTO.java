package com.alanpatrik.sghss.api.model.dto;

import com.alanpatrik.sghss.api.model.dto.response.ProfissionalSaudeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgendaDTO {

    private Long id;
    private ProfissionalSaudeResponseDTO profissionalSaude;
    private Set<HorarioDisponivelDTO> horariosDisponiveis;
}