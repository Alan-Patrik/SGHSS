package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.dto.response.PacienteResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
            Endereco endereco,
            LocalDateTime dataCriacao,
            LocalDateTime dataModificacao,
            List<Prontuario> prontuarios
    ) {
        super(nome, cpf, dataNascimento, telefone, email, endereco, dataCriacao, dataModificacao);
        this.prontuarios = prontuarios;
    }

    public static PacienteResponseDTO toResponseDTO(Paciente paciente) {
        var pacienteResponseDTO = new PacienteResponseDTO();
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
