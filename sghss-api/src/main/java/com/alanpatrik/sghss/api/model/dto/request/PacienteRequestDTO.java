package com.alanpatrik.sghss.api.model.dto.request;

import com.alanpatrik.sghss.api.model.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PacienteRequestDTO {
    private String nome;
    private String cpf;
    private String dataNascimento;
    private Endereco endereco;
    private String telefone;
    private String email;

}
