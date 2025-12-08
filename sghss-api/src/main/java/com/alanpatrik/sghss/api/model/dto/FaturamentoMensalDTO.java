package com.alanpatrik.sghss.api.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FaturamentoMensalDTO {
    private int ano;
    private int mes;
    private BigDecimal total;
}
