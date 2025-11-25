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
public class ExameRequestDTO {

    private String nomeUnidadeSaude;
    private TipoConsulta tipoConsulta;
    private LocalDateTime dataHoraExame;
    private TipoExame tipoExame;
    private String nomePaciente;
    private String observacao;
    private String CRM;
}
