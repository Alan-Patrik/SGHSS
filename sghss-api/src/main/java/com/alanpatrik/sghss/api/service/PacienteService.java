package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.Paciente;
import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.model.dto.HistoricoPacienteDTO;
import com.alanpatrik.sghss.api.model.dto.PacienteDTO;
import com.alanpatrik.sghss.api.model.dto.request.PacienteRequestDTO;
import com.alanpatrik.sghss.api.repository.PacienteRepository;
import com.alanpatrik.sghss.api.repository.ProntuarioRepository;
import com.alanpatrik.sghss.api.repository.UnidadeSaudeRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public List<PacienteDTO> findAll() {
        return pacienteRepository.findAll().stream().map(Paciente::toDTO).toList();
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public PacienteDTO findById(Long id) {
        var paciente = pacienteRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PACIENTE_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id)))
        );
        return Paciente.toDTO(paciente);
    }

    @RequireRoles({Constantes.PRIV_VISUALIZAR_PACIENTE})
    @Transactional(readOnly = true)
    public Paciente findByName(String nome) {
        return pacienteRepository.findByNome(nome).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PACIENTE_NOT_FOUND_BY_NAME_MESSAGE.replace("%s", nome))
        );
    }

    @RequireRoles({Constantes.PRIV_VISUALIZAR_HISTORICO})
    @Transactional(readOnly = true)
    public HistoricoPacienteDTO findByHistoricoClinico(Long id) {
        var paciente = pacienteRepository.findByHistoricoClinico(id).orElseThrow(
                () -> new InformacaoNaoEncontradaException(
                        Constantes.PACIENTE_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id)))
        );
        return Paciente.toHistoricoPacienteResponseDTO(paciente);
    }

    @RequireRoles({Constantes.PRIV_CADASTRAR_PACIENTE})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PacienteDTO save(PacienteRequestDTO pacienteRequestDTO) {
        this.validarParametrosObrigatorios(pacienteRequestDTO);

        if (verifyIfExistsByName(pacienteRequestDTO.getNome())) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        var unidadeSaude = unidadeSaudeRepository.findByNome(pacienteRequestDTO.getNomeUnidadeSaude())
                .orElseThrow(() -> new InformacaoNaoEncontradaException(
                        Constantes.UNIDADE_SAUDE_NOT_FOUND_BY_NAME_MESSAGE
                                .replace("%s", pacienteRequestDTO.getNomeUnidadeSaude())
                ));

        for (var pacienteUnidade : unidadeSaude.getPacientes()) {
            if (pacienteUnidade.getNome().equals(pacienteRequestDTO.getNome())) {
                throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
            }
        }

        var enderecoPaciente = pacienteRequestDTO.getEndereco();
        var endereco = Endereco.builder()
                .logradouro(enderecoPaciente.getLogradouro())
                .numero(enderecoPaciente.getNumero())
                .complemento(enderecoPaciente.getComplemento())
                .bairro(enderecoPaciente.getBairro())
                .cidade(enderecoPaciente.getCidade())
                .estado(enderecoPaciente.getEstado())
                .cep(enderecoPaciente.getCep())
                .build();

        var paciente = new Paciente(
                pacienteRequestDTO.getNome(),
                pacienteRequestDTO.getCpf(),
                pacienteRequestDTO.getDataNascimento(),
                pacienteRequestDTO.getTelefone(),
                pacienteRequestDTO.getEmail(),
                endereco,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                new LinkedHashSet<>());

        var prontuario = Prontuario.builder()
                .observacao("")
                .paciente(paciente)
                .prescricoes(new LinkedHashSet<>())
                .dataCriacao(LocalDateTime.now())
                .dataModificacao(LocalDateTime.now())
                .build();

        paciente.setProntuario(prontuario);
        paciente.getUnidades().add(unidadeSaude);
        paciente = pacienteRepository.save(paciente);

        prontuarioRepository.save(prontuario);

        unidadeSaude.getPacientes().add(paciente);
        unidadeSaudeRepository.save(unidadeSaude);


        return Paciente.toDTO(paciente);
    }

    @RequireRoles({Constantes.PRIV_ATUALIZAR_PACIENTE})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PacienteDTO update(Long id, PacienteRequestDTO pacienteRequestDTO) {
        this.validarParametrosObrigatorios(pacienteRequestDTO);

        var paciente = Paciente.toEntityResponse(this.findById(id));

        var unidadeSaude = unidadeSaudeRepository.findByNome(pacienteRequestDTO.getNomeUnidadeSaude())
                .orElseThrow(() -> new InformacaoNaoEncontradaException("Unidade de saúde não encontrada."));

        var existePaciente = false;
        for (var pacienteUnidade : unidadeSaude.getPacientes()) {
            if (paciente.getNome().equals(pacienteRequestDTO.getNome()) &&
                    pacienteUnidade.getNome().equals(paciente.getNome())) {
                existePaciente = true;
                break;
            }
        }

        if (!existePaciente) {
            throw new InformacaoNaoEncontradaException("O Paciente não pertence a Unidade de Saúde informada.");
        }

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
        return Paciente.toDTO(paciente);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var paciente = pacienteRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PACIENTE_NOT_FOUND_BY_ID_MESSAGE
                                .replace("%s", String.valueOf(id))));

        var prontuario = paciente.getProntuario();
        if (prontuario != null) {
            prontuario.setPaciente(null);
            paciente.setProntuario(null);
        }

        if (paciente.getConsultas() != null && !paciente.getConsultas().isEmpty()) {
            for (var consulta : new LinkedHashSet<>(paciente.getConsultas())) {
                consulta.setPaciente(null);
            }
            paciente.getConsultas().clear();
        }

        if (paciente.getUnidades() != null && !paciente.getUnidades().isEmpty()) {
            for (var unidadeSaude : new LinkedHashSet<>(paciente.getUnidades())) {
                unidadeSaude.getPacientes().removeIf(p -> p.getId().equals(id));
            }
            paciente.getUnidades().clear();
        }

        pacienteRepository.delete(paciente);

        return "Paciente deletado com sucesso.";
    }

    private boolean verifyIfExistsByName(String nome) {
        return pacienteRepository.existsPacienteByNome((nome));
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
        if (pacienteRequestDTO.getNomeUnidadeSaude() == null ||
                pacienteRequestDTO.getNomeUnidadeSaude().isEmpty() ||
                pacienteRequestDTO.getNomeUnidadeSaude().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome da unidade de saúde é obrigatório.");
        }
    }
}
