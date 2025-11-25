package com.alanpatrik.sghss.api.model.dto.request;

import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import com.alanpatrik.sghss.api.model.enums.TipoExame;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExameUpdateRequestDTO {

    private String nomeUnidadeSaude;
    private TipoConsulta tipoConsulta;
    private LocalDateTime dataHoraExameAntigo;
    private LocalDateTime dataHoraExameNovo;
    private TipoExame tipoExame;
    private String nomePaciente;
    private String observacao;
    private String CRM;
}
