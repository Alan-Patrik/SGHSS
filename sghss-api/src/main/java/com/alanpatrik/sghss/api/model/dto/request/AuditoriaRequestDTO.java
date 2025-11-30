package com.alanpatrik.sghss.api.model.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditoriaRequestDTO {

    private String usuario;
    private String acao;
    private String nomeEntidade;
    private String idEntidade;
    private String ip;
    private String detalhes;
}
