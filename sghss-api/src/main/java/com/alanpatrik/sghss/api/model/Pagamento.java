package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.PagamentoResponseDTO;
import com.alanpatrik.sghss.api.model.enums.FormaPagamento;
import com.alanpatrik.sghss.api.model.enums.StatusPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PAGAMENTO",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_PAGAMENTO_CONSULTA", columnNames = {"ID_CONSULTA"}),
                @UniqueConstraint(name = "UK_PAGAMENTO_EXAME", columnNames = {"ID_EXAME"})
        })
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PAGAMENTO", nullable = false)
    private Long id;

    @Column(name = "VALOR", precision = 12, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "DESCONTO", precision = 12, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(name = "ACRESCIMO", precision = 12, scale = 2)
    private BigDecimal acrescimo = BigDecimal.ZERO;

    @Column(name = "TOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "FORMA", nullable = false)
    private FormaPagamento forma;

    @Column(name = "DATA_PAGAMENTO")
    private LocalDateTime dataPagamento;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONSULTA", foreignKey = @ForeignKey(name = "FK_PAG_CONSULTA"))
    @JsonIgnore
    private Consulta consulta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXAME", foreignKey = @ForeignKey(name = "FK_PAG_EXAME"))
    @JsonIgnore
    private Exame exame;

    public static PagamentoResponseDTO toResponseDTO(Pagamento pagamento) {
        var pagamentoResponseDTO = new PagamentoResponseDTO();
        pagamentoResponseDTO.setId(pagamento.getId());
        pagamentoResponseDTO.setValor(pagamento.getValor());
        pagamentoResponseDTO.setTotal(pagamento.getTotal());
        pagamentoResponseDTO.setStatus(pagamento.getStatus().name());
        pagamentoResponseDTO.setForma(pagamento.getForma().name());
        pagamentoResponseDTO.setDataPagamento(pagamento.getDataPagamento());
        return pagamentoResponseDTO;
    }

    public void calcularTotal() {
        BigDecimal base = (valor != null ? valor : BigDecimal.ZERO);
        BigDecimal desc = (desconto != null ? desconto : BigDecimal.ZERO);
        BigDecimal acr = (acrescimo != null ? acrescimo : BigDecimal.ZERO);
        this.total = base.subtract(desc).add(acr);
    }
}
