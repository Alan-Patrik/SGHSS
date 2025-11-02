package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.dto.PacienteDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PACIENTE")
public class Paciente extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    private List<Prontuario> prontuarios;

    public Paciente(
            String nome,
            String cpf,
            String dataNascimento,
            String telefone,
            String email,
            Endereco endereco
    ) {
        super(nome, cpf, dataNascimento, telefone, email, endereco);
    }

    public static PacienteDTO toDTO(Paciente paciente) {
        var pacienteDTO = new PacienteDTO();
        pacienteDTO.setNome(paciente.getNome());
        pacienteDTO.setCpf(paciente.getCpf());
        pacienteDTO.setDataNascimento(paciente.getDataNascimento());
        pacienteDTO.setTelefone(paciente.getTelefone());
        pacienteDTO.setEmail(paciente.getEmail());
        pacienteDTO.setEndereco(paciente.getEndereco());
        return pacienteDTO;
    }

    public static Paciente toEntity(PacienteDTO pacienteDTO) {
        var paciente = new Paciente();
        paciente.setNome(pacienteDTO.getNome());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setEndereco(pacienteDTO.getEndereco());
        return paciente;
    }

//    public Consulta agendarConsulta(Consulta consulta) {
//
//    }
//
//    public Consulta cancelarConsulta() {
//
//    }
//
//    public void acessarTeleconsulta() {
//
//    }
}
