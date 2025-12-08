package com.alanpatrik.sghss.api.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArquivoExportadoDTO {
    private byte[] conteudo;
    private String nomeArquivo;
    private String contentType;
}
