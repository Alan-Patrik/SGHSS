package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.Paciente;
import com.alanpatrik.sghss.api.model.ProfissionalSaude;
import com.alanpatrik.sghss.api.model.UnidadeSaude;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoExame;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExameResponseDTO {

    private Long id;
    private String observacao;
    private TipoExame tipoExame;
    private TipoConsulta tipoConsulta;
    private LocalDateTime dataHoraExame;
    private Paciente paciente;
    private UnidadeSaude unidadeSaude;
    private ProfissionalSaude profissionalSaude;
}
