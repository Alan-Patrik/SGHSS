package com.alanpatrik.sghss.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "PRONTUARIO")
public class Prontuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRONTUARIO", nullable = false)
    private Long id;

    @Column(name = "DATA_PRONTUARIO", nullable = false)
    private LocalDate data;

    @Column(name = "TXT_OBSERVACAO", nullable = true)
    private String observacao;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_PACIENTE", nullable = true)
    private Paciente paciente;

    @OneToMany(mappedBy = "prontuario", fetch = FetchType.LAZY)
    private List<Prescricao> prescricao;

    public Prontuario(LocalDate data, String observacao, Paciente paciente) {
        this.data = data;
        this.observacao = observacao;
        this.paciente = paciente;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
