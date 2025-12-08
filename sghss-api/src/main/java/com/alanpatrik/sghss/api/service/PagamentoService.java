package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.model.Pagamento;
import com.alanpatrik.sghss.api.model.dto.request.PagamentoRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.PagamentoResponseDTO;
import com.alanpatrik.sghss.api.model.enums.StatusPagamento;
import com.alanpatrik.sghss.api.repository.ConsultaRepository;
import com.alanpatrik.sghss.api.repository.ExameRepository;
import com.alanpatrik.sghss.api.repository.PagamentoRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PagamentoService {
    private final ConsultaRepository consultaRepository;
    private final ExameRepository exameRepository;
    private final PagamentoRepository pagamentoRepository;

    @RequireRoles({Constantes.PRIV_PAGAR_CONSULTA})
    @Transactional
    public PagamentoResponseDTO pagarConsulta(Long consultaId, PagamentoRequestDTO pagamentoRequestDTO) {
        var consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new InformacaoNaoEncontradaException(
                        Constantes.CONSULTA_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(consultaId))
                ));

        if (consulta.getPagamento() != null &&
                consulta.getPagamento().getStatus() == StatusPagamento.PAGO) {
            throw new ConflitoException(
                    Constantes.CONSULTA_CONFLICT_BY_PAGAMENTO_MESSAGE.replace("%s", String.valueOf(consultaId))
            );
        }

        var pagamento = new Pagamento();
        pagamento.setValor(pagamentoRequestDTO.getValor());
        pagamento.setDesconto(pagamentoRequestDTO.getDesconto());
        pagamento.setAcrescimo(pagamentoRequestDTO.getAcrescimo());
        pagamento.setForma(pagamentoRequestDTO.getFormaPagamento());
        pagamento.calcularTotal();
        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setConsulta(consulta);

        consulta.vincularPagamento(pagamento);

        pagamentoRepository.save(pagamento);
        consultaRepository.save(consulta);

        return Pagamento.toResponseDTO(pagamento);
    }

    @RequireRoles({Constantes.PRIV_PAGAR_EXAME})
    @Transactional
    public PagamentoResponseDTO pagarExame(Long exameId, PagamentoRequestDTO pagamentoRequestDTO) {
        var exame = exameRepository.findById(exameId)
                .orElseThrow(() -> new InformacaoNaoEncontradaException(
                        Constantes.EXAME_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(exameId))));

        if (exame.getPagamento() != null &&
                exame.getPagamento().getStatus() == StatusPagamento.PAGO) {
            throw new ConflitoException(
                    Constantes.EXAME_CONFLICT_BY_PAGAMENTO_MESSAGE.replace("%s", String.valueOf(exameId)));
        }

        var pagamento = new Pagamento();
        pagamento.setValor(pagamentoRequestDTO.getValor());
        pagamento.setDesconto(pagamentoRequestDTO.getDesconto());
        pagamento.setAcrescimo(pagamentoRequestDTO.getAcrescimo());
        pagamento.setForma(pagamentoRequestDTO.getFormaPagamento());
        pagamento.calcularTotal();
        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setExame(exame);

        exame.vincularPagamento(pagamento);

        pagamentoRepository.save(pagamento);
        exameRepository.save(exame);

        return Pagamento.toResponseDTO(pagamento);
    }
}
