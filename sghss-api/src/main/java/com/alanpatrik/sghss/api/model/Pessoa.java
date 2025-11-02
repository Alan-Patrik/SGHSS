package com.alanpatrik.sghss.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "DATA_MODIFICACAO", nullable = false)
    private LocalDateTime dataModificacao;

}
