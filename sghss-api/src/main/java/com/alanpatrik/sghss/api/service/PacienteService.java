package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.dto.PacienteDTO;
import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.Paciente;
import com.alanpatrik.sghss.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(Paciente::toDTO)
                .toList();

    }

    public PacienteDTO findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }
        return Paciente.toDTO(pacienteRepository.findById(id).get());
    }

    public PacienteDTO findByHistoricoClinico(Long id) {
        return Paciente.toDTO(pacienteRepository.findByHistoricoClinico(id));
    }

    private boolean verifyIfExistsById(Long id) {
        return pacienteRepository.existsById(id);
    }

    private boolean verifyIfExistsByName(String nome) {
        return pacienteRepository.existsPacienteByNome((nome));
    }

    public PacienteDTO findByName(String nome) {
        return Paciente.toDTO(pacienteRepository.findByNome((nome)));
    }

    public PacienteDTO save(PacienteDTO pacienteDTO) throws Exception {
        if (verifyIfExistsByName(pacienteDTO.getNome())) {
            throw new Exception("Paciente já cadastrado!");
        }

        var endereco = Endereco.builder()
                .logradouro(pacienteDTO.getEndereco().getLogradouro())
                .numero(pacienteDTO.getEndereco().getNumero())
                .complemento(pacienteDTO.getEndereco().getComplemento())
                .bairro(pacienteDTO.getEndereco().getBairro())
                .cidade(pacienteDTO.getEndereco().getCidade())
                .estado(pacienteDTO.getEndereco().getEstado())
                .cep(pacienteDTO.getEndereco().getCep())
                .build();


        var paciente = new Paciente(
                pacienteDTO.getNome(),
                pacienteDTO.getCpf(),
                pacienteDTO.getDataNascimento(),
                pacienteDTO.getTelefone(),
                pacienteDTO.getEmail(),
                endereco
        );

        paciente = pacienteRepository.save(paciente);
        return Paciente.toDTO(paciente);
    }

    public PacienteDTO update(Long id, PacienteDTO pacienteDTO) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }

        var paciente = pacienteRepository.findById(id).get();
        var enderecoPaciente = pacienteDTO.getEndereco();
        var endereco = Endereco.builder()
                .logradouro(enderecoPaciente.getLogradouro())
                .numero(enderecoPaciente.getNumero())
                .complemento(enderecoPaciente.getComplemento())
                .bairro(enderecoPaciente.getBairro())
                .cidade(enderecoPaciente.getCidade())
                .estado(enderecoPaciente.getEstado())
                .cep(enderecoPaciente.getCep())
                .build();

        paciente.setNome(pacienteDTO.getNome());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setEndereco(endereco);

        paciente = pacienteRepository.save(paciente);
        return Paciente.toDTO(paciente);
    }

    public void delete(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }

        pacienteRepository.deleteById(id);
    }
}
