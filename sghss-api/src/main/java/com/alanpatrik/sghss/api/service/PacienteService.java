package com.alanpatrik.sghss.api.service;

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

    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    public Paciente findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }
        return pacienteRepository.findById(id).get();
    }

    public Paciente findByHistoricoClinico(Long id) {
        return pacienteRepository.findByHistoricoClinico(id);
    }

    private boolean verifyIfExistsById(Long id) {
        return pacienteRepository.existsById(id);
    }

    private boolean verifyIfExistsByName(String nome) {
        return pacienteRepository.existsPacienteByNome((nome));
    }

    public Paciente save(Paciente paciente) throws Exception {
        if (verifyIfExistsByName(paciente.getNome())) {
            throw new Exception("Paciente já cadastrado!");
        }

        var endereco = Endereco.builder()
                .logradouro(paciente.getEndereco().getLogradouro())
                .numero(paciente.getEndereco().getNumero())
                .complemento(paciente.getEndereco().getComplemento())
                .bairro(paciente.getEndereco().getBairro())
                .cidade(paciente.getEndereco().getCidade())
                .estado(paciente.getEndereco().getEstado())
                .cep(paciente.getEndereco().getCep())
                .build();


        var novoPaciente = new Paciente(
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getTelefone(),
                paciente.getEmail(),
                endereco
        );

        novoPaciente = pacienteRepository.save(novoPaciente);
        return novoPaciente;
    }

    public Paciente update(Long id, Paciente paciente) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }

        var pacienteAtualizado = pacienteRepository.findById(id).get();
        var endereco = Endereco.builder()
                .logradouro(paciente.getEndereco().getLogradouro())
                .numero(paciente.getEndereco().getNumero())
                .complemento(paciente.getEndereco().getComplemento())
                .bairro(paciente.getEndereco().getBairro())
                .cidade(paciente.getEndereco().getCidade())
                .estado(paciente.getEndereco().getEstado())
                .cep(paciente.getEndereco().getCep())
                .build();

        pacienteAtualizado.setNome(paciente.getNome());
        pacienteAtualizado.setCpf(paciente.getCpf());
        pacienteAtualizado.setDataNascimento(paciente.getDataNascimento());
        pacienteAtualizado.setTelefone(paciente.getTelefone());
        pacienteAtualizado.setEmail(paciente.getEmail());
        pacienteAtualizado.setEndereco(endereco);

        pacienteRepository.save(pacienteAtualizado);
        return pacienteAtualizado;
    }

    public void delete(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }

        pacienteRepository.deleteById(id);
    }
}
