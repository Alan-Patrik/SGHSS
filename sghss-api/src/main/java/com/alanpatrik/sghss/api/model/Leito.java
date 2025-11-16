package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.LeitoResponseDTO;
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

    public static LeitoResponseDTO toResponseDTO(Leito leito) {
        var leitoResponseDTO = new LeitoResponseDTO();
        leitoResponseDTO.setId(leito.getId());
        leitoResponseDTO.setNumero(leito.getNumero());
        leitoResponseDTO.setUnidadeSaude(leito.getUnidadeSaude());
        return leitoResponseDTO;
    }

    public static Leito toEntity(LeitoResponseDTO leitoResponseDTO) {
        var leito = new Leito();
        leito.setId(leitoResponseDTO.getId());
        leito.setNumero(leitoResponseDTO.getNumero());
        leito.setUnidadeSaude(leitoResponseDTO.getUnidadeSaude());
        return leito;
    }
}
