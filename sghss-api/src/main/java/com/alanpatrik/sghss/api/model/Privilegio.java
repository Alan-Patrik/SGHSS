package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.PrivilegioResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "PRIVILEGIO")
public class Privilegio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRIVILEGIO")
    private Long id;

    @Column(name = "TXT_PRIVILEGIO", nullable = false, unique = true, length = 150)
    private String name;

    @OneToMany(mappedBy = "privilegio", fetch = FetchType.LAZY)
    private Set<RolePrivilegio> rolePrivilegios = new HashSet<>();

    public static PrivilegioResponseDTO toResponseDTO(Privilegio privilegio) {
        var privilegioDTO = new PrivilegioResponseDTO();
        privilegioDTO.setId(privilegio.getId());
        privilegioDTO.setName(privilegio.getName());
        return privilegioDTO;
    }
}

