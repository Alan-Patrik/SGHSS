package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.HistoricoPacienteDTO;
import com.alanpatrik.sghss.api.model.dto.PacienteDTO;
import com.alanpatrik.sghss.api.model.dto.response.PacienteResponseDTO;
import com.alanpatrik.sghss.api.security.crypto.Crypto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Prontuario prontuario;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "PACIENTE_UNIDADE_SAUDE",
            joinColumns = @JoinColumn(name = "ID_PACIENTE", referencedColumnName = "ID_PACIENTE"),
            inverseJoinColumns = @JoinColumn(name = "ID_UNIDADE_SAUDE", referencedColumnName = "ID_UNIDADE_SAUDE")
    )
    @JsonIgnore
    private Set<UnidadeSaude> unidades = new LinkedHashSet<>();


    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Consulta> consultas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Exame> exames = new LinkedHashSet<>();

    public Paciente(
            String nome,
            String cpf,
            String dataNascimento,
            String telefone,
            String email,
            Endereco endereco,
            LocalDateTime dataCriacao,
            LocalDateTime dataModificacao,
            Prontuario prontuario,
            Set<UnidadeSaude> unidades,
            Set<Consulta> consultas,
            Set<Exame> exames
    ) {
        super(nome, cpf, dataNascimento, telefone, email, endereco, dataCriacao, dataModificacao);
        this.prontuario = prontuario;
        this.unidades = unidades;
        this.consultas = consultas;
        this.exames = exames;
    }

    public static PacienteResponseDTO toResponseDTO(Paciente paciente) {
        var pacienteResponseDTO = new PacienteResponseDTO();
        pacienteResponseDTO.setId(paciente.getId());
        pacienteResponseDTO.setNome(paciente.getNome());
        return pacienteResponseDTO;
    }

    public static HistoricoPacienteDTO toHistoricoPacienteResponseDTO(Paciente paciente) {
        var historicoPacienteResponseDTO = new HistoricoPacienteDTO();
        historicoPacienteResponseDTO.setId(paciente.getId());
        historicoPacienteResponseDTO.setNome(paciente.getNome());
        historicoPacienteResponseDTO.setCpf(Crypto.mascararCpf(paciente.getCpf()));
        historicoPacienteResponseDTO.setDataNascimento(paciente.getDataNascimento());
        historicoPacienteResponseDTO.setTelefone(paciente.getTelefone());
        historicoPacienteResponseDTO.setEmail(Crypto.mascararEmail(paciente.getEmail()));
        historicoPacienteResponseDTO.setEndereco(paciente.getEndereco());
        historicoPacienteResponseDTO.setDataCriacao(paciente.getDataCriacao());
        historicoPacienteResponseDTO.setDataModificacao(paciente.getDataModificacao());
        historicoPacienteResponseDTO.setProntuario(Prontuario.toResponseDTO(paciente.getProntuario()));
        historicoPacienteResponseDTO.setExames(Exame.toResponseDTOList(paciente.getExames()));
        return historicoPacienteResponseDTO;
    }

    public static Paciente toEntity(PacienteResponseDTO pacienteResponseDTO) {
        var paciente = new Paciente();
        paciente.setId(pacienteResponseDTO.getId());
        paciente.setNome(pacienteResponseDTO.getNome());
        return paciente;
    }

    public static Set<PacienteResponseDTO> toResponseDTOList(Set<Paciente> pacientes) {
        var pacienteDTOs = new LinkedHashSet<PacienteResponseDTO>();
        for (Paciente paciente : pacientes) {
            pacienteDTOs.add(toResponseDTO(paciente));
        }
        return pacienteDTOs;
    }

    public static Set<Paciente> toEntityList(Set<PacienteResponseDTO> pacienteResponseDTOs) {
        var pacientes = new LinkedHashSet<Paciente>();
        for (var pacienteResponseDTO : pacienteResponseDTOs) {
            pacientes.add(toEntity(pacienteResponseDTO));
        }
        return pacientes;
    }

    public static PacienteDTO toDTO(Paciente paciente) {
        var pacienteDTO = new PacienteDTO();
        pacienteDTO.setId(paciente.getId());
        pacienteDTO.setNome(paciente.getNome());
        pacienteDTO.setCpf(Crypto.mascararCpf(paciente.getCpf()));
        pacienteDTO.setDataNascimento(paciente.getDataNascimento());
        pacienteDTO.setTelefone(paciente.getTelefone());
        pacienteDTO.setEmail(Crypto.mascararEmail(paciente.getEmail()));
        pacienteDTO.setEndereco(paciente.getEndereco());
        pacienteDTO.setDataCriacao(paciente.getDataCriacao());
        pacienteDTO.setDataModificacao(paciente.getDataModificacao());
        return pacienteDTO;
    }

    public static Paciente toEntityResponse(PacienteDTO pacienteDTO) {
        var paciente = new Paciente();
        paciente.setId(pacienteDTO.getId());
        paciente.setNome(pacienteDTO.getNome());
        paciente.setCpf(Crypto.mascararCpf(pacienteDTO.getCpf()));
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEmail(Crypto.mascararEmail(pacienteDTO.getEmail()));
        paciente.setEndereco(pacienteDTO.getEndereco());
        paciente.setDataCriacao(pacienteDTO.getDataCriacao());
        paciente.setDataModificacao(pacienteDTO.getDataModificacao());
        return paciente;
    }
}
