package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.LeitoResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany
    @Column(name = "ID_PACIENTE", nullable = false)
    private List<Paciente> pacientes;

    @OneToMany
    @Column(name = "ID_PROFISSIONAL_SAUDE", nullable = false)
    private List<ProfissionalSaude> profissionaisSaude;

    @ManyToOne
    private UnidadeSaude unidadeSaude;

    public static LeitoResponseDTO toResponseDTO(Leito leito) {
        var leitoResponseDTO = new LeitoResponseDTO();
        leitoResponseDTO.setId(leito.getId());
        leitoResponseDTO.setNumero(leito.getNumero());
        leitoResponseDTO.setUnidadeSaude(leito.getUnidadeSaude());
        leitoResponseDTO.setPacientes(Paciente.toResponseDTOList(leito.getPacientes()));
        leitoResponseDTO.setProfissionaisSaude(ProfissionalSaude.toResponseDTOList(leito.profissionaisSaude));
        return leitoResponseDTO;
    }

    public static Leito toEntity(LeitoResponseDTO leitoResponseDTO) {
        var leito = new Leito();
        leito.setId(leitoResponseDTO.getId());
        leito.setNumero(leitoResponseDTO.getNumero());
        leito.setUnidadeSaude(leitoResponseDTO.getUnidadeSaude());
        leito.setPacientes(Paciente.toEntityList(leitoResponseDTO.getPacientes()));
        leito.setProfissionaisSaude(ProfissionalSaude.toEntityList(leitoResponseDTO.getProfissionaisSaude()));
        return leito;
    }
}
