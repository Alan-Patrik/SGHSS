package com.alanpatrik.sghss.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AUDITORIA")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AUDITORIA", nullable = false)
    private Long id;

    @Column(name = "TXT_USUARIO", nullable = false)
    private String usuario;

    @Column(name = "TXT_ACAO", nullable = false)
    private String acao;

    @Column(name = "TXT_NOME_ENTIDADE", nullable = false)
    private String nomeEntidade;

    @Column(name = "TXT_ID_ENTIDADE", nullable = false)
    private String idEntidade;

    @Column(name = "TXT_IP", nullable = false)
    private String ip;

    @Column(name = "TXT_DETALHES", nullable = false, length = 2000)
    private String detalhes;

    @Column(name = "DAT_DATA_HORA", nullable = false)
    private LocalDateTime dataHora;
}
