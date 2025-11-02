package com.alanpatrik.sghss.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PRESCRICAO")
public class Prescricao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRESCRICAO", nullable = false)
    private Long id;

    @Column(name = "TXT_NOME_MEDICAMENTO", nullable = false)
    private String medicamento;

    @Column(name = "TXT_OBSERVACAO", nullable = false)
    private String observacao;

    @Column(name = "TXT_DOSAGEM", nullable = false)
    private String dosagem;

    @Column(name = "TXT_DURACAO_MEDICACAO", nullable = false)
    private String duracao;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_PRONTUARIO")
    private Prontuario prontuario;
}
