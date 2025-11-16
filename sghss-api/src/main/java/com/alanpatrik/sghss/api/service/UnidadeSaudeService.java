package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.UnidadeSaude;
import com.alanpatrik.sghss.api.model.dto.request.UnidadeSaudeRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.UnidadeSaudeResponseDTO;
import com.alanpatrik.sghss.api.repository.UnidadeSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UnidadeSaudeService {

    private final UnidadeSaudeRepository unidadeSaudeRepository;

    public List<UnidadeSaudeResponseDTO> getAll() {
        return unidadeSaudeRepository.findAll().stream().map(UnidadeSaude::toResponseDTO).toList();
    }

    public UnidadeSaudeResponseDTO findById(Long id) {
        var unidadeSaudeResponseDTO = unidadeSaudeRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return UnidadeSaude.toResponseDTO(unidadeSaudeResponseDTO);
    }

    public UnidadeSaudeResponseDTO findByName(String nome) {
        var unidadeSaudeResponseDTO = unidadeSaudeRepository.findByNome(nome).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return UnidadeSaude.toResponseDTO(unidadeSaudeResponseDTO);
    }

    public UnidadeSaudeResponseDTO save(UnidadeSaudeRequestDTO unidadeSaudeRequestDTO) {
        this.validarParametrosObrigatorios(unidadeSaudeRequestDTO);
        if (unidadeSaudeRepository.existsUnidadeSaudeByNome(unidadeSaudeRequestDTO.getNome())) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        var enderecoRequestDTO = unidadeSaudeRequestDTO.getEndereco();
        var endereco = Endereco.builder()
                .logradouro(enderecoRequestDTO.getLogradouro())
                .numero(enderecoRequestDTO.getNumero())
                .complemento(enderecoRequestDTO.getComplemento())
                .bairro(enderecoRequestDTO.getBairro())
                .cidade(enderecoRequestDTO.getCidade())
                .estado(enderecoRequestDTO.getEstado())
                .cep(enderecoRequestDTO.getCep())
                .build();

        var unidadeSaude = UnidadeSaude.builder()
                .nome(unidadeSaudeRequestDTO.getNome())
                .endereco(endereco)
                .profissionais(new ArrayList<>())
                .profissionais(new ArrayList<>())
                .build();

        unidadeSaude = unidadeSaudeRepository.save(unidadeSaude);
        return UnidadeSaude.toResponseDTO(unidadeSaude);
    }

    public UnidadeSaudeResponseDTO update(Long id, UnidadeSaudeRequestDTO unidadeSaudeRequestDTO) {
        this.validarParametrosObrigatorios(unidadeSaudeRequestDTO);
        var unidadeSaude = UnidadeSaude.toEntityDTO(this.findById(id));

        if (unidadeSaudeRepository.existsUnidadeSaudeByNome(unidadeSaudeRequestDTO.getNome())) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        unidadeSaude.setNome(unidadeSaudeRequestDTO.getNome());
        unidadeSaude.setEndereco(unidadeSaudeRequestDTO.getEndereco());
        unidadeSaude = unidadeSaudeRepository.save(unidadeSaude);

        return UnidadeSaude.toResponseDTO(unidadeSaude);
    }

    public void delete(Long id) {
        var unidadeSaude = this.findById(id);
        unidadeSaudeRepository.deleteById(unidadeSaude.getId());
    }

    private void validarParametrosObrigatorios(UnidadeSaudeRequestDTO unidadeSaudeRequestDTO) {
        if (unidadeSaudeRequestDTO.getNome() == null ||
                unidadeSaudeRequestDTO.getNome().isEmpty() ||
                unidadeSaudeRequestDTO.getNome().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome da Unidade de Saúde é obrigatório.");
        }

        var endereco = unidadeSaudeRequestDTO.getEndereco();
        if (endereco.getLogradouro() == null ||
                endereco.getLogradouro().isEmpty()) {
            throw new ParametroInvalidoException("O campo Logradouro é obrigatório.");
        }
        if (endereco.getNumero() == null ||
                endereco.getNumero().isEmpty()) {
            throw new ParametroInvalidoException("O campo Número é obrigatório.");
        }
        if (endereco.getBairro() == null ||
                endereco.getBairro().isEmpty()) {
            throw new ParametroInvalidoException("O campo Bairro é obrigatório.");
        }
        if (endereco.getCidade() == null ||
                endereco.getCidade().isEmpty()) {
            throw new ParametroInvalidoException("O campo Cidade é obrigatório.");
        }
        if (endereco.getEstado() == null ||
                endereco.getEstado().isEmpty()) {
            throw new ParametroInvalidoException("O campo Estado é obrigatório.");
        }
        if (endereco.getCep() == null || endereco.getCep().isEmpty()) {
            throw new ParametroInvalidoException("O campo Cep é obrigatório.");
        }
    }
}
