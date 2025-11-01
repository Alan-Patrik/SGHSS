package com.alanpatrik.sghss.api.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
@Table(name = "ENDERECO")
public class Endereco {

    @Column(name = "TXT_LOGRADOURO", nullable = false)
    private String logradouro;

    @Column(name = "NUM_NUMERO", nullable = false)
    private String numero;

    @Column(name = "TXT_COMPLEMENTO", nullable = true)
    private String complemento;

    @Column(name = "TXT_BAIRRO", nullable = false)
    private String bairro;

    @Column(name = "TXT_CIDADE", nullable = false)
    private String cidade;

    @Column(name = "TXT_ESTADO", nullable = false)
    private String estado;

    @Column(name = "NUM_CEP", nullable = false)
    private String cep;

}
