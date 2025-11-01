package com.alanpatrik.sghss.api.model;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany
    @JoinColumn(name = "ID_PRONTUARIO", nullable = true)
    private List<Prontuario> historicoClinico;

    public Paciente(
            String nome,
            String cpf,
            String dataNascimento,
            String telefone,
            String email,
            Endereco endereco,
            List<Prontuario> historicoClinico
    ) {
        super(nome, cpf, dataNascimento, telefone, email, endereco);
        this.historicoClinico = historicoClinico;
    }

//    public Consulta agendarConsulta(Consulta consulta) {
//
//    }
//
//    public Consulta cancelarConsulta() {
//
//    }
//
//    public List<Prontuario> visualizarHistorico() {
//        return this.historicoClinico;
//    }
//
//    public void acessarTeleconsulta() {
//
//    }
}
