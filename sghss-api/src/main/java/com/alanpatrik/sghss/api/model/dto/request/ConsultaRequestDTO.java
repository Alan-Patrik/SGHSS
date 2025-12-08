package com.alanpatrik.sghss.api.model.dto.request;

import com.alanpatrik.sghss.api.model.enums.AreaAtuacao;
import com.alanpatrik.sghss.api.model.enums.TipoConsulta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsultaRequestDTO {

    private LocalDateTime dataHoraConsulta;
    private AreaAtuacao areaAtuacao;
    private TipoConsulta tipoConsulta;
    private String nomePaciente;
    private String nomeUnidadeSaude;
    private String CRM;
}
