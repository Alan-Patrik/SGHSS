package com.alanpatrik.sghss.api.model.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagamentoResponseDTO {
    private Long id;
    private java.math.BigDecimal valor;
    private java.math.BigDecimal total;
    private String status;
    private String forma;
    private java.time.LocalDateTime dataPagamento;
}
