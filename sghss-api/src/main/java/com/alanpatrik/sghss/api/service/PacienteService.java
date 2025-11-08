package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.dto.request.PacienteRequestDTO;
import com.alanpatrik.sghss.api.dto.response.PacienteResponseDTO;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.Paciente;
import com.alanpatrik.sghss.api.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public List<PacienteResponseDTO> findAll() {
        return pacienteRepository.findAll().stream().map(Paciente::toResponseDTO).toList();
    }

    public PacienteResponseDTO findById(Long id) {
        var paciente = pacienteRepository.findById(id).orElseThrow(() -> new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return Paciente.toResponseDTO(paciente);
    }

    public Paciente findByName(String nome) {
        return pacienteRepository.findByNome(nome).orElseThrow(() -> new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
    }

    public PacienteResponseDTO findByHistoricoClinico(Long id) {
        return Paciente.toResponseDTO(pacienteRepository.findByHistoricoClinico(id));
    }

    private boolean verifyIfExistsByName(String nome) {
        return pacienteRepository.existsPacienteByNome((nome));
    }

    public PacienteResponseDTO save(PacienteRequestDTO pacienteRequestDTO) {
        this.validarParametrosObrigatorios(pacienteRequestDTO);

        if (verifyIfExistsByName(pacienteRequestDTO.getNome())) {
            throw new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE);
        }

        var enderecoPaciente = pacienteRequestDTO.getEndereco();
        var endereco = Endereco.builder().logradouro(enderecoPaciente.getLogradouro()).numero(enderecoPaciente.getNumero()).complemento(enderecoPaciente.getComplemento()).bairro(enderecoPaciente.getBairro()).cidade(enderecoPaciente.getCidade()).estado(enderecoPaciente.getEstado()).cep(enderecoPaciente.getCep()).build();


        var paciente = new Paciente(pacienteRequestDTO.getNome(), pacienteRequestDTO.getCpf(), pacienteRequestDTO.getDataNascimento(), pacienteRequestDTO.getTelefone(), pacienteRequestDTO.getEmail(), endereco, LocalDateTime.now(), LocalDateTime.now(), null);

        paciente = pacienteRepository.save(paciente);
        return Paciente.toResponseDTO(paciente);
    }

    public PacienteResponseDTO update(Long id, PacienteRequestDTO pacienteRequestDTO) {
        this.validarParametrosObrigatorios(pacienteRequestDTO);

        var paciente = Paciente.toEntity(this.findById(id));
        var enderecoPaciente = pacienteRequestDTO.getEndereco();
        var endereco = Endereco.builder().logradouro(enderecoPaciente.getLogradouro()).numero(enderecoPaciente.getNumero()).complemento(enderecoPaciente.getComplemento()).bairro(enderecoPaciente.getBairro()).cidade(enderecoPaciente.getCidade()).estado(enderecoPaciente.getEstado()).cep(enderecoPaciente.getCep()).build();

        paciente.setNome(pacienteRequestDTO.getNome());
        paciente.setCpf(pacienteRequestDTO.getCpf());
        paciente.setDataNascimento(pacienteRequestDTO.getDataNascimento());
        paciente.setTelefone(pacienteRequestDTO.getTelefone());
        paciente.setEmail(pacienteRequestDTO.getEmail());
        paciente.setEndereco(endereco);
        paciente.setDataModificacao(LocalDateTime.now());

        paciente = pacienteRepository.save(paciente);
        return Paciente.toResponseDTO(paciente);
    }

    private void validarParametrosObrigatorios(PacienteRequestDTO pacienteRequestDTO) {
        if (pacienteRequestDTO.getNome() == null || pacienteRequestDTO.getNome().isEmpty()) {
            throw new ParametroInvalidoException("O campo Nome é obrigatório.");
        }
        if (pacienteRequestDTO.getCpf() == null || pacienteRequestDTO.getCpf().isEmpty()) {
            throw new ParametroInvalidoException("O campo CPF é obrigatório.");
        }
        if (pacienteRequestDTO.getDataNascimento() == null || pacienteRequestDTO.getDataNascimento().isEmpty()) {
            throw new ParametroInvalidoException("O campo Data de nascimento é obrigatório.");
        }
        if (pacienteRequestDTO.getTelefone() == null || pacienteRequestDTO.getTelefone().isEmpty()) {
            throw new ParametroInvalidoException("O campo Telefone é obrigatório.");
        }
        if (pacienteRequestDTO.getEmail() == null || pacienteRequestDTO.getEmail().isEmpty()) {
            throw new ParametroInvalidoException("O campo Email é obrigatório.");
        }
        if (pacienteRequestDTO.getEndereco().getLogradouro() == null ||
                pacienteRequestDTO.getEndereco().getLogradouro().isEmpty()) {
            throw new ParametroInvalidoException("O campo Logradouro é obrigatório.");
        }
        if (pacienteRequestDTO.getEndereco().getNumero() == null ||
                pacienteRequestDTO.getEndereco().getNumero().isEmpty()) {
            throw new ParametroInvalidoException("O campo Número é obrigatório.");
        }
        if (pacienteRequestDTO.getEndereco().getBairro() == null ||
                pacienteRequestDTO.getEndereco().getBairro().isEmpty()) {
            throw new ParametroInvalidoException("O campo Bairro é obrigatório.");
        }
        if (pacienteRequestDTO.getEndereco().getCidade() == null ||
                pacienteRequestDTO.getEndereco().getCidade().isEmpty()) {
            throw new ParametroInvalidoException("O campo Cidade é obrigatório.");
        }
        if (pacienteRequestDTO.getEndereco().getEstado() == null ||
                pacienteRequestDTO.getEndereco().getEstado().isEmpty()) {
            throw new ParametroInvalidoException("O campo Estado é obrigatório.");
        }
        if (pacienteRequestDTO.getEndereco().getCep() == null || pacienteRequestDTO.getEndereco().getCep().isEmpty()) {
            throw new ParametroInvalidoException("O campo Cep é obrigatório.");
        }
    }
}
