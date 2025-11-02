package com.alanpatrik.sghss.api.model;

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
