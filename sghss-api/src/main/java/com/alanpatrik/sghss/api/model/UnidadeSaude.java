package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.UnidadeSaudeResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "UNIDADE_SAUDE")
public class UnidadeSaude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INIDADE_SAUDE", nullable = false)
    private Long id;

    @Column(name = "TXT_NOME", nullable = false)
    private String nome;

    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "unidadeSaude")
    private List<Leito> leitos;

    @OneToMany(mappedBy = "unidadeSaude")
    private List<ProfissionalSaude> profissionais;

    public static UnidadeSaudeResponseDTO toResponseDTO(UnidadeSaude unidadeSaude) {
        var unidadeSaudeResponseDTO = new UnidadeSaudeResponseDTO();
        unidadeSaudeResponseDTO.setId(unidadeSaude.getId());
        unidadeSaudeResponseDTO.setNome(unidadeSaude.getNome());
        unidadeSaudeResponseDTO.setEndereco(unidadeSaude.getEndereco());
        unidadeSaudeResponseDTO.setProfissionais(unidadeSaude.getProfissionais());
        unidadeSaudeResponseDTO.setLeitos(unidadeSaude.getLeitos());

        return unidadeSaudeResponseDTO;
    }

    public static UnidadeSaude toEntity(UnidadeSaudeResponseDTO unidadeSaudeResponseDTO) {
        var unidadeSaude = new UnidadeSaude();
        unidadeSaude.setId(unidadeSaudeResponseDTO.getId());
        unidadeSaude.setNome(unidadeSaudeResponseDTO.getNome());
        unidadeSaude.setEndereco(unidadeSaudeResponseDTO.getEndereco());
        unidadeSaude.setProfissionais(unidadeSaudeResponseDTO.getProfissionais());
        unidadeSaude.setLeitos(unidadeSaudeResponseDTO.getLeitos());

        return unidadeSaude;
    }
}
