package com.alanpatrik.sghss.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Pessoa {
    @Column(name = "TXT_NOME", nullable = false)
    protected String nome;

    @Column(name = "NUM_CPF", nullable = false)
    protected String cpf;

    @Column(name = "DATA_NASCIMENTO", nullable = false)
    protected String dataNascimento;

    @Column(name = "NUM_TELEFONE", nullable = false)
    protected String telefone;

    @Column(name = "TXT_EMAIL", nullable = false)
    protected String email;

    @Embedded
    protected Endereco endereco;

}
