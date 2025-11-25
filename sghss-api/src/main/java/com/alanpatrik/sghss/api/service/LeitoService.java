package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Leito;
import com.alanpatrik.sghss.api.model.Paciente;
import com.alanpatrik.sghss.api.model.ProfissionalSaude;
import com.alanpatrik.sghss.api.model.dto.request.LeitoAdicionarPacienteRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.LeitoAdicionarProfissionalRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.LeitoRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.LeitoUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.LeitoResponseDTO;
import com.alanpatrik.sghss.api.repository.LeitoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LeitoService {

    private final LeitoRepository leitoRepository;
    private final UnidadeSaudeService unidadeSaudeService;
    private final ProfissionalSaudeService profissionalSaudeService;
    private final PacienteService pacienteService;

    public List<LeitoResponseDTO> getAll() {
        return leitoRepository.findAll().stream().map(Leito::toResponseDTO).toList();
    }

    public LeitoResponseDTO findById(Long id) {
        var leito = leitoRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return Leito.toResponseDTO(leito);
    }

    public LeitoResponseDTO findByNumero(String numero) {
        var leito = leitoRepository.findLeitoByNumero(numero).orElseThrow(() ->
                new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE));
        return Leito.toResponseDTO(leito);
    }

    public LeitoResponseDTO save(LeitoRequestDTO leitoRequestDTO) {
        this.validarParametrosObrigatorios(leitoRequestDTO);
        this.existsLeitoByNumero(leitoRequestDTO.getNumero());

        var unidadeSaude = unidadeSaudeService.findByName(leitoRequestDTO.getNomeUnidadeSaude());
        var leito = Leito.builder()
                .numero(leitoRequestDTO.getNumero())
                .unidadeSaude(unidadeSaude)
                .pacientes(new ArrayList<>())
                .profissionaisSaude(new ArrayList<>())
                .build();

        leito = leitoRepository.save(leito);
        return Leito.toResponseDTO(leito);
    }

    public LeitoResponseDTO addProfissionalSaude(LeitoAdicionarProfissionalRequestDTO leitoAdicionarProfissionalRequestDTO) {
        this.validarParametrosObrigatorios(leitoAdicionarProfissionalRequestDTO);

        var leito = Leito.toEntity(this.findByNumero(leitoAdicionarProfissionalRequestDTO.getNumeroLeito()));
        var profissionalSaude = profissionalSaudeService.findByCRM(leitoAdicionarProfissionalRequestDTO.getCRM());

        var profissionaisSaude = new ArrayList<ProfissionalSaude>();
        if (leito.getProfissionaisSaude() != null && !leito.getProfissionaisSaude().isEmpty()) {
            for (var profissionalSaudeDTO : leito.getProfissionaisSaude()) {
                if (profissionalSaudeDTO.getCRM().equals(leitoAdicionarProfissionalRequestDTO.getCRM())) {
                    throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
                }
                profissionaisSaude.add(profissionalSaudeDTO);
            }
        }

        profissionaisSaude.add(profissionalSaude);
        leito.setProfissionaisSaude(profissionaisSaude);
        leito = leitoRepository.save(leito);

        return Leito.toResponseDTO(leito);
    }

    public LeitoResponseDTO addPaciente(LeitoAdicionarPacienteRequestDTO leitoAdicionarPacienteRequestDTO) {
        this.validarParametrosObrigatorios(leitoAdicionarPacienteRequestDTO);

        var leito = Leito.toEntity(this.findByNumero(leitoAdicionarPacienteRequestDTO.getNumeroLeito()));
        var paciente = pacienteService.findByName(leitoAdicionarPacienteRequestDTO.getNomePaciente());

        var pacientes = new ArrayList<Paciente>();
        if (leito.getPacientes() != null && !leito.getPacientes().isEmpty()) {
            for (var pacienteDTO : leito.getPacientes()) {
                if (pacienteDTO.getNome().equals(leitoAdicionarPacienteRequestDTO.getNomePaciente())) {
                    throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
                }
                pacientes.add(pacienteDTO);
            }
        }

        pacientes.add(paciente);
        leito.setPacientes(pacientes);
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

    public LeitoResponseDTO deleteProfissionalSaude(String crm, String numeroLeito) {
        if (numeroLeito == null || numeroLeito.isEmpty() || numeroLeito.isBlank()) {
            throw new ParametroInvalidoException("O campo Número do leito é obrigatório.");
        }

        var leito = Leito.toEntity(this.findByNumero(numeroLeito));
        var profissionalSaude = profissionalSaudeService.findByCRM(crm);

        var profissionaisSaude = new ArrayList<ProfissionalSaude>();
        var containsProfissionalSaude = false;
        if (leito.getProfissionaisSaude() != null) {
            for (var profissionalSaudeDTO : leito.getProfissionaisSaude()) {
                if (!profissionalSaudeDTO.getCRM().equals(crm)) {
                    profissionaisSaude.add(profissionalSaudeDTO);
                } else {
                    profissionaisSaude.remove(profissionalSaudeDTO);
                    containsProfissionalSaude = true;
                }
            }
        }

        if (!containsProfissionalSaude) {
            throw new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE);
        }

        leito.setProfissionaisSaude(profissionaisSaude);
        leito = leitoRepository.save(leito);

        leito.getProfissionaisSaude().remove(profissionalSaude);
        return Leito.toResponseDTO(leito);
    }

    public LeitoResponseDTO deletePaciente(String nomePaciente, String numeroLeito) {
        this.validarParametrosObrigatorios(numeroLeito, nomePaciente);

        var leito = Leito.toEntity(this.findByNumero(numeroLeito));
        var paciente = pacienteService.findByName(nomePaciente);

        var pacientes = new ArrayList<Paciente>();
        var containsPaciente = false;
        if (leito.getPacientes() != null) {
            for (var pacienteDTO : leito.getPacientes()) {
                if (!pacienteDTO.getNome().equals(nomePaciente)) {
                    pacientes.add(pacienteDTO);
                } else {
                    pacientes.remove(pacienteDTO);
                    containsPaciente = true;
                }
            }
        }

        if (!containsPaciente) {
            throw new InformacaoNaoEncontradaException(Constantes.NOT_FOUND_MESSAGE);
        }

        leito.setPacientes(pacientes);
        leito = leitoRepository.save(leito);

        leito.getPacientes().remove(paciente);
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

    private void validarParametrosObrigatorios(LeitoAdicionarProfissionalRequestDTO leitoAdicionarProfissionalRequestDTO) {
        if (leitoAdicionarProfissionalRequestDTO.getNumeroLeito() == null ||
                leitoAdicionarProfissionalRequestDTO.getNumeroLeito().isEmpty() ||
                leitoAdicionarProfissionalRequestDTO.getNumeroLeito().isBlank()) {
            throw new ParametroInvalidoException("O campo Número do leito é obrigatório.");
        }
        if (leitoAdicionarProfissionalRequestDTO.getCRM() == null ||
                leitoAdicionarProfissionalRequestDTO.getCRM().isEmpty() ||
                leitoAdicionarProfissionalRequestDTO.getCRM().isBlank()) {
            throw new ParametroInvalidoException("O campo CRM do Profissional de Saúde é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(LeitoAdicionarPacienteRequestDTO leitoAdicionarPacienteRequestDTO) {
        if (leitoAdicionarPacienteRequestDTO.getNumeroLeito() == null ||
                leitoAdicionarPacienteRequestDTO.getNumeroLeito().isEmpty() ||
                leitoAdicionarPacienteRequestDTO.getNumeroLeito().isBlank()) {
            throw new ParametroInvalidoException("O campo Número do leito é obrigatório.");
        }
        if (leitoAdicionarPacienteRequestDTO.getNomePaciente() == null ||
                leitoAdicionarPacienteRequestDTO.getNomePaciente().isEmpty() ||
                leitoAdicionarPacienteRequestDTO.getNomePaciente().isBlank()) {
            throw new ParametroInvalidoException("O campo Nome do paciente é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(String numeroLeito, String nomePaciente) {
        if (numeroLeito == null || numeroLeito.isEmpty() || numeroLeito.isBlank()) {
            throw new ParametroInvalidoException("O campo Número do leito é obrigatório.");
        }
        if (nomePaciente == null || nomePaciente.isEmpty() || nomePaciente.isBlank()) {
            throw new ParametroInvalidoException("O campo Nome do paciente é obrigatório.");
        }
    }
}