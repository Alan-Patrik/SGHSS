package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.enums.AreaAtuacao;
import com.alanpatrik.sghss.api.model.enums.StatusConsulta;
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
public class ConsultaResponseDTO {
    private Long id;
    private LocalDateTime dataHoraConsulta;
    private AreaAtuacao areaAtuacao;
    private TipoConsulta tipoConsulta;
    private StatusConsulta statusConsulta;
    private String nomePaciente;
    private String CRM;
}
