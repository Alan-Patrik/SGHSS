package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.ProfissionalSaude;
import com.alanpatrik.sghss.api.model.dto.request.ProfissionalSaudeRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.ProfissionalSaudeResponseDTO;
import com.alanpatrik.sghss.api.repository.ProfissionalSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository profissionalSaudeRepository;

    public List<ProfissionalSaudeResponseDTO> findAll() {
        return profissionalSaudeRepository.findAll().stream().map(ProfissionalSaude::toResponseDTO).toList();
    }

    public ProfissionalSaudeResponseDTO findById(Long id) {
        var profissionalSaude = profissionalSaudeRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return ProfissionalSaude.toResponseDTO(profissionalSaude);
    }

    public ProfissionalSaude findByCRM(String nome) {
        return profissionalSaudeRepository.findByCRM(nome).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
    }

    private boolean verifyIfExistsByName(String nome) {
        return profissionalSaudeRepository.existsProfissionalSaudeByNome((nome));
    }

    public ProfissionalSaudeResponseDTO save(ProfissionalSaudeRequestDTO profissionalSaudeRequestDTO) {
        this.validarParametrosObrigatorios(profissionalSaudeRequestDTO);

        if (verifyIfExistsByName(profissionalSaudeRequestDTO.getNome())) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        var enderecoProfissionalSaude = profissionalSaudeRequestDTO.getEndereco();
        var endereco = Endereco.builder()
                .logradouro(enderecoProfissionalSaude.getLogradouro())
                .numero(enderecoProfissionalSaude.getNumero())
                .complemento(enderecoProfissionalSaude.getComplemento())
                .bairro(enderecoProfissionalSaude.getBairro())
                .cidade(enderecoProfissionalSaude.getCidade())
                .estado(enderecoProfissionalSaude.getEstado())
                .cep(enderecoProfissionalSaude.getCep())
                .build();

        var profissionalSaude = new ProfissionalSaude(
                profissionalSaudeRequestDTO.getNome(),
                profissionalSaudeRequestDTO.getCpf(),
                profissionalSaudeRequestDTO.getDataNascimento(),
                profissionalSaudeRequestDTO.getTelefone(),
                profissionalSaudeRequestDTO.getEmail(),
                endereco,
                LocalDateTime.now(),
                LocalDateTime.now(),
                profissionalSaudeRequestDTO.getEspecialidade(),
                profissionalSaudeRequestDTO.getAreaAtuacao(),
                new ArrayList<>(),
                profissionalSaudeRequestDTO.getCRM(),
                null);

        profissionalSaude = profissionalSaudeRepository.save(profissionalSaude);
        return ProfissionalSaude.toResponseDTO(profissionalSaude);
    }

    public ProfissionalSaudeResponseDTO update(Long id, ProfissionalSaudeRequestDTO profissionalSaudeRequestDTO) {
        this.validarParametrosObrigatorios(profissionalSaudeRequestDTO);

        var profissionalSaude = ProfissionalSaude.toEntity(this.findById(id));
        var enderecoProfissionalSaude = profissionalSaudeRequestDTO.getEndereco();
        var endereco = Endereco.builder()
                .logradouro(enderecoProfissionalSaude.getLogradouro())
                .numero(enderecoProfissionalSaude.getNumero())
                .complemento(enderecoProfissionalSaude.getComplemento())
                .bairro(enderecoProfissionalSaude.getBairro())
                .cidade(enderecoProfissionalSaude.getCidade())
                .estado(enderecoProfissionalSaude.getEstado())
                .cep(enderecoProfissionalSaude.getCep())
                .build();

        profissionalSaude.setNome(profissionalSaudeRequestDTO.getNome());
        profissionalSaude.setCpf(profissionalSaudeRequestDTO.getCpf());
        profissionalSaude.setDataNascimento(profissionalSaudeRequestDTO.getDataNascimento());
        profissionalSaude.setTelefone(profissionalSaudeRequestDTO.getTelefone());
        profissionalSaude.setEmail(profissionalSaudeRequestDTO.getEmail());
        profissionalSaude.setEndereco(endereco);
        profissionalSaude.setDataModificacao(LocalDateTime.now());
        profissionalSaude.setEspecialidade(profissionalSaudeRequestDTO.getEspecialidade());
        profissionalSaude.setCRM(profissionalSaudeRequestDTO.getCRM());

        profissionalSaude = profissionalSaudeRepository.save(profissionalSaude);
        return ProfissionalSaude.toResponseDTO(profissionalSaude);
    }

    private void validarParametrosObrigatorios(ProfissionalSaudeRequestDTO profissionalSaudeRequestDTO) {
        if (profissionalSaudeRequestDTO.getNome() == null || profissionalSaudeRequestDTO.getNome().isEmpty()) {
            throw new ParametroInvalidoException("O campo Nome é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getCpf() == null || profissionalSaudeRequestDTO.getCpf().isEmpty()) {
            throw new ParametroInvalidoException("O campo CPF é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getDataNascimento() == null || profissionalSaudeRequestDTO.getDataNascimento().isEmpty()) {
            throw new ParametroInvalidoException("O campo Data de nascimento é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getTelefone() == null || profissionalSaudeRequestDTO.getTelefone().isEmpty()) {
            throw new ParametroInvalidoException("O campo Telefone é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getEmail() == null || profissionalSaudeRequestDTO.getEmail().isEmpty()) {
            throw new ParametroInvalidoException("O campo Email é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getEndereco().getLogradouro() == null ||
                profissionalSaudeRequestDTO.getEndereco().getLogradouro().isEmpty()) {
            throw new ParametroInvalidoException("O campo Logradouro é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getEndereco().getNumero() == null ||
                profissionalSaudeRequestDTO.getEndereco().getNumero().isEmpty()) {
            throw new ParametroInvalidoException("O campo Número é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getEndereco().getBairro() == null ||
                profissionalSaudeRequestDTO.getEndereco().getBairro().isEmpty()) {
            throw new ParametroInvalidoException("O campo Bairro é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getEndereco().getCidade() == null ||
                profissionalSaudeRequestDTO.getEndereco().getCidade().isEmpty()) {
            throw new ParametroInvalidoException("O campo Cidade é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getEndereco().getEstado() == null ||
                profissionalSaudeRequestDTO.getEndereco().getEstado().isEmpty()) {
            throw new ParametroInvalidoException("O campo Estado é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getEndereco().getCep() == null || profissionalSaudeRequestDTO.getEndereco().getCep().isEmpty()) {
            throw new ParametroInvalidoException("O campo Cep é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getEspecialidade() == null) {
            throw new ParametroInvalidoException("O campo Especialidade é obrigatório.");
        }
        if (profissionalSaudeRequestDTO.getCRM() == null || profissionalSaudeRequestDTO.getCRM().isEmpty()) {
            throw new ParametroInvalidoException("O campo CRM é obrigatório.");
        }
    }
}
