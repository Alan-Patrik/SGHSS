package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Usuario;
import com.alanpatrik.sghss.api.model.UsuarioRole;
import com.alanpatrik.sghss.api.model.dto.request.UsuarioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.UsuarioUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.UsuarioResponseDTO;
import com.alanpatrik.sghss.api.repository.RoleRepository;
import com.alanpatrik.sghss.api.repository.UsuarioRepository;
import com.alanpatrik.sghss.api.repository.UsuarioRoleRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UsuarioRoleRepository usuarioRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream().map(Usuario::toResponseDTO).toList();
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findByUsername(String username) {
        var usuario = usuarioRepository.loadUserGraph(username).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.USUARIO_NOT_FOUND_BY_USERNAME_MESSAGE.replace("%s", username)
                ));

        return Usuario.toResponseDTO(usuario);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String save(UsuarioRequestDTO usuarioRequestDTO) {
        this.validarParametrosObrigatorios(usuarioRequestDTO);
        this.validarInformacoesUsuario(usuarioRequestDTO.getUsername(), usuarioRequestDTO.getEmail());

        var usuario = Usuario.builder()
                .username(usuarioRequestDTO.getUsername())
                .email(usuarioRequestDTO.getEmail())
                .password(passwordEncoder.encode(usuarioRequestDTO.getPassword()))
                .usuarioRoles(Set.of())
                .dataCriacao(LocalDateTime.now())
                .dataModificacao(LocalDateTime.now())
                .status(true)
                .build();

        usuarioRepository.save(usuario);

        return "Usuário criado com sucesso!";
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String update(Long id, UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        this.validarParametrosObrigatorios(usuarioUpdateRequestDTO);

        var usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.USUARIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        this.validarInformacoesUsuario(usuarioUpdateRequestDTO.getUsername(), usuarioUpdateRequestDTO.getEmail());

        usuario.setUsername(usuarioUpdateRequestDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioUpdateRequestDTO.getPassword()));
        usuario.setEmail(usuarioUpdateRequestDTO.getEmail());
        usuario.setDataModificacao(LocalDateTime.now());

        usuarioRepository.save(usuario);

        return "Usuário atualizado com sucesso!";
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.USUARIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id)))
        );

        usuario.setStatus(false);
        usuario.setDataModificacao(LocalDateTime.now());

        usuarioRepository.save(usuario);

        return "Usuário deletado com sucesso.";
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String assignRoleToUser(Long userId, Long roleId) {
        var usuario = usuarioRepository.findById(userId).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.USUARIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(userId))
                ));

        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.ROLE_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(roleId))
                ));

        var usuarioRole = UsuarioRole.builder()
                .role(role)
                .usuario(usuario)
                .build();

        usuarioRoleRepository.save(usuarioRole);
        return "Role atribuída ao usuário com sucesso.";
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String removeRoleFromUser(Long userId, Long roleId) {
        var usuario = usuarioRepository.findById(userId).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.USUARIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(userId))));

        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.ROLE_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(roleId))));

        usuario.getUsuarioRoles().removeIf(usuarioRole ->
                usuarioRole.getRole().getId().equals(roleId));

        var usuarioRole = UsuarioRole.builder()
                .role(role)
                .usuario(usuario)
                .build();

        usuarioRoleRepository.delete(usuarioRole);
        return "Role removido do usuário com sucesso.";
    }

    private void validarInformacoesUsuario(String username, String email) {
        usuarioRepository.findByUsername(username).ifPresent(u -> {
            if (u.getUsername().equals(username)) {
                throw new ConflitoException("Usuário com o Username já existe: " + username);
            }
        });

        usuarioRepository.findByEmail(email).ifPresent(u -> {
            if (u.getEmail().equals(email)) {
                throw new ConflitoException("Usuário com o email já existe: " + email);
            }
        });
    }

    private void validarParametrosObrigatorios(UsuarioRequestDTO usuarioRequestDTO) {
        if (usuarioRequestDTO.getUsername() == null ||
                usuarioRequestDTO.getUsername().isEmpty() ||
                usuarioRequestDTO.getUsername().isBlank()) {
            throw new ParametroInvalidoException("O campo Username é obrigatório.");
        }
        if (usuarioRequestDTO.getPassword() == null ||
                usuarioRequestDTO.getPassword().isEmpty() ||
                usuarioRequestDTO.getPassword().isBlank()) {
            throw new ParametroInvalidoException("O campo Password é obrigatório.");
        }
        if (usuarioRequestDTO.getEmail() == null ||
                usuarioRequestDTO.getEmail().isEmpty() ||
                usuarioRequestDTO.getEmail().isBlank()) {
            throw new ParametroInvalidoException("O campo Password é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        if (usuarioUpdateRequestDTO.getUsername() == null ||
                usuarioUpdateRequestDTO.getUsername().isEmpty() ||
                usuarioUpdateRequestDTO.getUsername().isBlank()) {
            throw new ParametroInvalidoException("O campo Username é obrigatório.");
        }
        if (usuarioUpdateRequestDTO.getPassword() == null ||
                usuarioUpdateRequestDTO.getPassword().isEmpty() ||
                usuarioUpdateRequestDTO.getPassword().isBlank()) {
            throw new ParametroInvalidoException("O campo Password é obrigatório.");
        }
        if (usuarioUpdateRequestDTO.getEmail() == null ||
                usuarioUpdateRequestDTO.getEmail().isEmpty() ||
                usuarioUpdateRequestDTO.getEmail().isBlank()) {
            throw new ParametroInvalidoException("O campo Password é obrigatório.");
        }
    }
}

