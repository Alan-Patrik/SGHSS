package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.ProfissionalSaude;
import com.alanpatrik.sghss.api.model.dto.HorarioDisponivelDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgendaResponseDTO {
    private Long id;
    private ProfissionalSaude profissionalSaude;
    private List<HorarioDisponivelDTO> horariosDisponiveis;
}