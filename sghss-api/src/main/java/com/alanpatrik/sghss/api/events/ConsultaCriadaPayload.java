package com.alanpatrik.sghss.api.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ConsultaCriadaPayload {
    private String email;
    private String nomePaciente;
    private String especialidade;
    private LocalDateTime dataHora;
    private String tipoConsulta;
    private String meetingUrl;
    private LocalDateTime expiraEm;
    private String local;
}
