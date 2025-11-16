package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Leito;
import com.alanpatrik.sghss.api.model.UnidadeSaude;
import com.alanpatrik.sghss.api.model.dto.request.LeitoRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.LeitoUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.LeitoResponseDTO;
import com.alanpatrik.sghss.api.repository.LeitoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LeitoService {

    private final LeitoRepository leitoRepository;
    private final UnidadeSaudeService unidadeSaudeService;

    public List<LeitoResponseDTO> getAll() {
        return leitoRepository.findAll().stream().map(Leito::toResponseDTO).toList();
    }

    public LeitoResponseDTO findById(Long id) {
        var leito = leitoRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return Leito.toResponseDTO(leito);
    }

    public LeitoResponseDTO save(LeitoRequestDTO leitoRequestDTO) {
        this.validarParametrosObrigatorios(leitoRequestDTO);
        this.existsLeitoByNumero(leitoRequestDTO.getNumero());

        var unidadeSaude = unidadeSaudeService.findByName(leitoRequestDTO.getNomeUnidadeSaude());
        var leito = Leito.builder()
                .numero(leitoRequestDTO.getNumero())
                .unidadeSaude(UnidadeSaude.toEntity(unidadeSaude))
                .build();
        leito = leitoRepository.save(leito);
        return Leito.toResponseDTO(leito);
    }

    public LeitoResponseDTO update(Long id, LeitoUpdateRequestDTO leitoUpdateRequestDTO) {
        if (leitoUpdateRequestDTO.getNumero() == null ||
                leitoUpdateRequestDTO.getNumero().isEmpty() ||
                leitoUpdateRequestDTO.getNumero().isBlank()) {
            throw new ParametroInvalidoException("O campo Número do leito é obrigatório.");
        }

        var leito = Leito.toEntity(this.findById(id));
        this.existsLeitoByNumero(leitoUpdateRequestDTO.getNumero());

        leito.setNumero(leitoUpdateRequestDTO.getNumero());
        leito = leitoRepository.save(leito);

        return Leito.toResponseDTO(leito);
    }

    public void delete(Long id) {
        var leito = this.findById(id);
        leitoRepository.deleteById(leito.getId());
    }

    private void existsLeitoByNumero(String numero) {
        if (leitoRepository.existsLeitoByNumero(numero)) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }
    }

    private void validarParametrosObrigatorios(LeitoRequestDTO leitoRequestDTO) {
        if (leitoRequestDTO.getNumero() == null ||
                leitoRequestDTO.getNumero().isEmpty() ||
                leitoRequestDTO.getNumero().isBlank()) {
            throw new ParametroInvalidoException("O campo Número do leito é obrigatório.");
        }
        if (leitoRequestDTO.getNomeUnidadeSaude() == null ||
                leitoRequestDTO.getNomeUnidadeSaude().isEmpty() ||
                leitoRequestDTO.getNomeUnidadeSaude().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome da Unidade de Saúde é obrigatório.");
        }
    }
}