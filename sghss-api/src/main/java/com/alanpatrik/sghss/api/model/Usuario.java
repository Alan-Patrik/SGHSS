package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.UsuarioResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "TXT_USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "TXT_PASSWORD", nullable = false)
    private String password;

    @Column(name = "TXT_EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "DAT_DATA_CRIACAO")
    private LocalDateTime dataCriacao;

    @Column(name = "DAT_DATA_MODIFICACAO")
    private LocalDateTime dataModificacao;

    @Column(name = "IND_STATUS")
    private boolean status;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioRole> usuarioRoles;

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        var usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setUsername(usuario.getUsername());
        usuarioResponseDTO.setPassword(usuario.getPassword());
        usuarioResponseDTO.setEmail(usuario.getEmail());
        usuarioResponseDTO.setUsuarioRoles(usuario.getUsuarioRoles());
        return usuarioResponseDTO;
    }

    @Transient
    public Set<Role> getRoles() {
        Set<Role> set = new HashSet<>();
        for (var usuarioRole : usuarioRoles) {
            set.add(usuarioRole.getRole());
        }
        return set;
    }
}

