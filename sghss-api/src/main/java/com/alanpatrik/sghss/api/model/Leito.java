package com.alanpatrik.sghss.api.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LEITO")
public class Leito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LEITO", nullable = false)
    private Long id;

    @Column(name = "TXT_NUMERO", nullable = false)
    private String numero;

    @ManyToOne
    private UnidadeSaude unidadeSaude;
}
