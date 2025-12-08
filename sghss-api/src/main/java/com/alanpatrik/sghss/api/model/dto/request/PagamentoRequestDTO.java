package com.alanpatrik.sghss.api.model.dto.request;

import com.alanpatrik.sghss.api.model.enums.FormaPagamento;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagamentoRequestDTO {
    private BigDecimal valor;
    private BigDecimal desconto;
    private BigDecimal acrescimo;
    private FormaPagamento formaPagamento;
}
