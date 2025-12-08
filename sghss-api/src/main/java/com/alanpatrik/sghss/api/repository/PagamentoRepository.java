package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Pagamento;
import com.alanpatrik.sghss.api.model.dto.FaturamentoMensalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query("""
                select new com.alanpatrik.sghss.api.model.dto.FaturamentoMensalDTO(
                    YEAR(p.dataPagamento), MONTH(p.dataPagamento), SUM(p.total)
                )
                from Pagamento p
                where p.status = com.alanpatrik.sghss.api.model.enums.StatusPagamento.PAGO
                  and p.dataPagamento between :inicio and :fim
                group by YEAR(p.dataPagamento), MONTH(p.dataPagamento)
                order by YEAR(p.dataPagamento), MONTH(p.dataPagamento)
            """)
    List<FaturamentoMensalDTO> faturamentoMensal(LocalDateTime inicio, LocalDateTime fim);
}
