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

    public boolean verifyIfExistsById(Long id) throws Exception {
        return pacienteRepository.existsById(id);
    }

    public boolean verifyIfExistsByName(String nome) throws Exception {
        return pacienteRepository.existsPacienteByNome((nome));
    }

    public Paciente save(Paciente paciente) throws Exception {
        if (verifyIfExistsByName(paciente.getNome())) {
            throw new Exception("Paciente já cadastrado!");
        }

        var novoPaciente = new Paciente();
        var endereco = new Endereco();

        endereco.setLogradouro(paciente.getEndereco().getLogradouro());
        endereco.setNumero(paciente.getEndereco().getNumero());
        endereco.setComplemento(paciente.getEndereco().getComplemento());
        endereco.setBairro(paciente.getEndereco().getBairro());
        endereco.setCidade(paciente.getEndereco().getCidade());
        endereco.setEstado(paciente.getEndereco().getEstado());
        endereco.setCep(paciente.getEndereco().getCep());

        novoPaciente.setNome(paciente.getNome());
        novoPaciente.setCpf(paciente.getCpf());
        novoPaciente.setDataNascimento(paciente.getDataNascimento());
        novoPaciente.setTelefone(paciente.getTelefone());
        novoPaciente.setEmail(paciente.getEmail());
        novoPaciente.setEndereco(endereco);

        pacienteRepository.save(novoPaciente);
        return novoPaciente;
    }

    public Paciente update(Long id, Paciente paciente) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Paciente não cadastrado!");
        }

        var pacienteAtualizado = pacienteRepository.findById(id).get();
        var endereco = new Endereco();

        endereco.setLogradouro(paciente.getEndereco().getLogradouro());
        endereco.setNumero(paciente.getEndereco().getNumero());
        endereco.setComplemento(paciente.getEndereco().getComplemento());
        endereco.setBairro(paciente.getEndereco().getBairro());
        endereco.setCidade(paciente.getEndereco().getCidade());
        endereco.setEstado(paciente.getEndereco().getEstado());
        endereco.setCep(paciente.getEndereco().getCep());

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
