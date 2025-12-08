package com.alanpatrik.sghss.api.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FaturamentoPorProfissionalDTO {
    private Long profissionalId;
    private String nomeProfissional;
    private BigDecimal total;
}
