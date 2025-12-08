package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Agenda;
import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.ProfissionalSaude;
import com.alanpatrik.sghss.api.model.UnidadeSaude;
import com.alanpatrik.sghss.api.model.dto.ProfissionalSaudeDTO;
import com.alanpatrik.sghss.api.model.dto.request.ProfissionalSaudeRequestDTO;
import com.alanpatrik.sghss.api.repository.AgendaRepository;
import com.alanpatrik.sghss.api.repository.ProfissionalSaudeRepository;
import com.alanpatrik.sghss.api.repository.UnidadeSaudeRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository profissionalSaudeRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;
    private final AgendaRepository agendaRepository;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public List<ProfissionalSaudeDTO> findAll() {
        return profissionalSaudeRepository.findAll().stream().map(ProfissionalSaude::toDTO).toList();
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public ProfissionalSaudeDTO findById(Long id) {
        var profissionalSaude = profissionalSaudeRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PROFISSIONAL_SAUDE_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id)))
        );
        return ProfissionalSaude.toDTO(profissionalSaude);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public ProfissionalSaude findByCRM(String crm) {
        return profissionalSaudeRepository.findByCRM(crm).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PROFISSIONAL_SAUDE_NOT_FOUND_BY_CRM_MESSAGE.replace("%s", crm))
        );
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProfissionalSaudeDTO save(ProfissionalSaudeRequestDTO profissionalSaudeRequestDTO) {
        this.validarParametrosObrigatorios(profissionalSaudeRequestDTO);

        if (verifyIfExistsByName(profissionalSaudeRequestDTO.getNome())) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        if (verifyIfExistsByCRM(profissionalSaudeRequestDTO.getCRM())) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        var unidadeSaude = unidadeSaudeRepository.findByNome(profissionalSaudeRequestDTO.getNomeUnidadeSaude())
                .orElseThrow(() -> new InformacaoNaoEncontradaException(
                        Constantes.UNIDADE_SAUDE_NOT_FOUND_BY_NAME_MESSAGE
                                .replace("%s", profissionalSaudeRequestDTO.getNomeUnidadeSaude())
                ));

        for (var profissionalSaudeUnidade : unidadeSaude.getProfissionais()) {
            if (profissionalSaudeUnidade.getNome().equals(profissionalSaudeRequestDTO.getNome())) {
                throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
            }
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
                new LinkedHashSet<>(),
                profissionalSaudeRequestDTO.getCRM(),
                null,
                new LinkedHashSet<>());

        var agenda = Agenda.builder()
                .profissionalSaude(profissionalSaude)
                .horariosDisponiveis(new LinkedHashSet<>())
                .build();

        profissionalSaude.setAgenda(agenda);
        profissionalSaude.getUnidades().add(unidadeSaude);
        profissionalSaude = profissionalSaudeRepository.save(profissionalSaude);

        unidadeSaude.getProfissionais().add(profissionalSaude);
        unidadeSaudeRepository.save(unidadeSaude);

        agendaRepository.save(agenda);

        return ProfissionalSaude.toDTO(profissionalSaude);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProfissionalSaudeDTO update(Long id, ProfissionalSaudeRequestDTO profissionalSaudeRequestDTO) {
        this.validarParametrosObrigatorios(profissionalSaudeRequestDTO);

        var profissionalSaude = ProfissionalSaude.toEntityResponse(this.findById(id));
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
        return ProfissionalSaude.toDTO(profissionalSaude);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public void addUnidadeSaude(String CRM, UnidadeSaude unidadeSaude) {
        var profissionalSaude = this.findByCRM(CRM);
        profissionalSaude.getUnidades().add(unidadeSaude);
        profissionalSaudeRepository.save(profissionalSaude);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public void removeUnidadeSaude(String CRM, UnidadeSaude unidadeSaude) {
        var profissionalSaude = this.findByCRM(CRM);
        profissionalSaude.getUnidades().remove(unidadeSaude);
        profissionalSaudeRepository.save(profissionalSaude);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var profissionalSaude = profissionalSaudeRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PROFISSIONAL_SAUDE_NOT_FOUND_BY_ID_MESSAGE
                                .replace("%s", String.valueOf(id))));

        var agenda = profissionalSaude.getAgenda();
        if (agenda != null) {
            agenda.setProfissionalSaude(null);
            profissionalSaude.setAgenda(null);
        }

        if (profissionalSaude.getConsultas() != null && !profissionalSaude.getConsultas().isEmpty()) {
            for (var consulta : new LinkedHashSet<>(profissionalSaude.getConsultas())) {
                consulta.setProfissionalSaude(null);
            }
            profissionalSaude.getConsultas().clear();
        }

        if (profissionalSaude.getUnidades() != null && !profissionalSaude.getUnidades().isEmpty()) {
            for (var unidadeSaude : new LinkedHashSet<>(profissionalSaude.getUnidades())) {
                unidadeSaude.getProfissionais().removeIf(p -> p.getId().equals(id));
            }
            profissionalSaude.getUnidades().clear();
        }

        profissionalSaudeRepository.delete(profissionalSaude);

        return "Profissional de saúde deletado com sucesso.";
    }

    private boolean verifyIfExistsByName(String nome) {
        return profissionalSaudeRepository.existsProfissionalSaudeByNome((nome));
    }

    private boolean verifyIfExistsByCRM(String CRM) {
        return profissionalSaudeRepository.existsProfissionalSaudeByCRM((CRM));
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
        if (profissionalSaudeRequestDTO.getNomeUnidadeSaude() == null ||
                profissionalSaudeRequestDTO.getNomeUnidadeSaude().isEmpty() ||
                profissionalSaudeRequestDTO.getNomeUnidadeSaude().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome da unidade de saúde é obrigatório.");
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
