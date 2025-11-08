package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.dto.response.PacienteResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @OneToOne(mappedBy = "paciente")
    private Prontuario prontuario;

    public Paciente(
            String nome,
            String cpf,
            String dataNascimento,
            String telefone,
            String email,
            Endereco endereco,
            LocalDateTime dataCriacao,
            LocalDateTime dataModificacao,
            Prontuario prontuario
    ) {
        super(nome, cpf, dataNascimento, telefone, email, endereco, dataCriacao, dataModificacao);
        this.prontuario = prontuario;
    }

    public static PacienteResponseDTO toResponseDTO(Paciente paciente) {
        var pacienteResponseDTO = new PacienteResponseDTO();
        pacienteResponseDTO.setId(paciente.getId());
        pacienteResponseDTO.setNome(paciente.getNome());
        pacienteResponseDTO.setCpf(paciente.getCpf());
        pacienteResponseDTO.setDataNascimento(paciente.getDataNascimento());
        pacienteResponseDTO.setTelefone(paciente.getTelefone());
        pacienteResponseDTO.setEmail(paciente.getEmail());
        pacienteResponseDTO.setEndereco(paciente.getEndereco());
        pacienteResponseDTO.setDataCriacao(paciente.getDataCriacao());
        pacienteResponseDTO.setDataModificacao(paciente.getDataModificacao());
        return pacienteResponseDTO;
    }

    public static Paciente toEntity(PacienteResponseDTO pacienteResponseDTO) {
        var paciente = new Paciente();
        paciente.setId(pacienteResponseDTO.getId());
        paciente.setNome(pacienteResponseDTO.getNome());
        paciente.setCpf(pacienteResponseDTO.getCpf());
        paciente.setDataNascimento(pacienteResponseDTO.getDataNascimento());
        paciente.setTelefone(pacienteResponseDTO.getTelefone());
        paciente.setEmail(pacienteResponseDTO.getEmail());
        paciente.setEndereco(pacienteResponseDTO.getEndereco());
        paciente.setDataCriacao(pacienteResponseDTO.getDataCriacao());
        paciente.setDataModificacao(pacienteResponseDTO.getDataModificacao());
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
