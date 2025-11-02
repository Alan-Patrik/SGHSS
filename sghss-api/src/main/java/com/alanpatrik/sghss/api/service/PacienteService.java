package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.dto.request.PacienteRequestDTO;
import com.alanpatrik.sghss.api.dto.response.PacienteResponseDTO;
import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.Paciente;
import com.alanpatrik.sghss.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteResponseDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(Paciente::toResponseDTO)
                .toList();

    }

    public PacienteResponseDTO findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }
        return Paciente.toResponseDTO(pacienteRepository.findById(id).get());
    }

    public PacienteResponseDTO findByHistoricoClinico(Long id) {
        return Paciente.toResponseDTO(pacienteRepository.findByHistoricoClinico(id));
    }

    private boolean verifyIfExistsById(Long id) {
        return pacienteRepository.existsById(id);
    }

    private boolean verifyIfExistsByName(String nome) {
        return pacienteRepository.existsPacienteByNome((nome));
    }

    public PacienteResponseDTO findByName(String nome) {
        return Paciente.toResponseDTO(pacienteRepository.findByNome((nome)));
    }

    public PacienteResponseDTO save(PacienteRequestDTO pacienteRequestDTO) throws Exception {
        if (verifyIfExistsByName(pacienteRequestDTO.getNome())) {
            throw new Exception("Paciente já cadastrado!");
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
                null
        );

        paciente = pacienteRepository.save(paciente);
        return Paciente.toResponseDTO(paciente);
    }

    public PacienteResponseDTO update(Long id, PacienteRequestDTO pacienteRequestDTO) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }

        var paciente = pacienteRepository.findById(id).get();
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

    public void delete(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }

        pacienteRepository.deleteById(id);
    }
}
