package com.alanpatrik.sghss.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RelatorioFinanceiroDTO {
    private List<FaturamentoMensalDTO> mensal;
    private List<FaturamentoPorProfissionalDTO> porProfissional;
    private BigDecimal totalGeral;
}
