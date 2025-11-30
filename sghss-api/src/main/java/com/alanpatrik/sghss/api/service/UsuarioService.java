package com.alanpatrik.sghss.api.service;
//
//import com.alanpatrik.sghss.api.model.dto.request.LoginRequestDTO;
//import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
//import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
//import com.alanpatrik.sghss.api.model.Usuario;
//import com.alanpatrik.sghss.api.model.dto.request.AuditoriaRequestDTO;
//import com.alanpatrik.sghss.api.model.dto.request.UsuarioRequestDTO;
//import com.alanpatrik.sghss.api.model.dto.response.TokenResponseDTO;
//import com.alanpatrik.sghss.api.repository.UsuarioRepository;
//import com.alanpatrik.sghss.api.util.LogonUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.UUID;
//
//import static com.alanpatrik.sghss.api.security.crypto.CpfCrypto.encryptCpf;
//import static com.alanpatrik.sghss.api.security.crypto.EmailCrypto.encryptEmail;
//import static com.alanpatrik.sghss.api.security.crypto.Generatekey.generateKey;
//
//@Service
//@RequiredArgsConstructor
//public class UsuarioService {
//
//    private final UsuarioRepository usuarioRepository;
//    private final PasswordEncoder encoder;
//    private final AuditoriaService auditoriaService;
//
//    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO) {
//        var usuario = usuarioRepository.findByUsername(loginRequestDTO.getUsername())
//                .orElseThrow(() -> new InformacaoNaoEncontradaException("Usuário não encontrado."));
//
//        if (!encoder.matches(loginRequestDTO.getPassword(), usuario.getPassword())) {
//            throw new ParametroInvalidoException("Credenciais inválidas.");
//        }
//
//        var token = jwtService.gerar(usuario.getUsername(), usuario.getRoles().stream().toList());
//        var refresh = UUID.randomUUID().toString();
//
//        registrarAuditoria(usuario.getUsername(), "LOGIN", usuario.getId());
//
//        return TokenResponseDTO.builder()
//                .accessToken(token)
//                .refreshToken(refresh)
//                .expiresIn(60L * 30)
//                .build();
//    }
//
//    public String registrarUsuario(UsuarioRequestDTO usuarioRequestDTO) throws Exception {
//        if (usuarioRepository.findByUsername(usuarioRequestDTO.getUsername()).isPresent()) {
//            throw new ParametroInvalidoException("Usuário já cadastrado.");
//        }
//
//        SecretKey key = generateKey();
//        var novoUsuario = Usuario.builder()
//                .username(usuarioRequestDTO.getUsername())
//                .password(encoder.encode(usuarioRequestDTO.getPassword()))
//                .email(encryptEmail(usuarioRequestDTO.getEmail(), key))
//                .cpf(encryptCpf(usuarioRequestDTO.getCpf(), key))
//                .roles(Set.of(LogonUtil.LABEL_CONSULTAR_CLIENTE))
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .status(true)
//                .build();
//
//        usuarioRepository.save(novoUsuario);
//
//        registrarAuditoria(novoUsuario.getUsername(), "CADASTRO", novoUsuario.getId());
//
//        return "Usuário criado com sucesso!";
//    }
//
//    private void registrarAuditoria(String usuario, String acao, Long idEntidade) {
//        var auditoriaRequestDTO = AuditoriaRequestDTO.builder()
//                .usuario(usuario)
//                .acao(acao)
//                .nomeEntidade("Usuario")
//                .idEntidade(String.valueOf(idEntidade))
//                .ip("-")
//                .detalhes("sucesso")
//                .build();
//
//        auditoriaService.save(auditoriaRequestDTO);
//    }
//}


import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Usuario;
import com.alanpatrik.sghss.api.model.UsuarioRole;
import com.alanpatrik.sghss.api.model.UsuarioRoleId;
import com.alanpatrik.sghss.api.model.dto.request.AuditoriaRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.UsuarioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.request.UsuarioUpdateRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.UsuarioResponseDTO;
import com.alanpatrik.sghss.api.repository.RoleRepository;
import com.alanpatrik.sghss.api.repository.UsuarioRepository;
import com.alanpatrik.sghss.api.repository.UsuarioRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UsuarioRoleRepository usuarioRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    @Transactional(readOnly = true)
    public UsuarioResponseDTO findByUsername(String username) {
        var usuario = usuarioRepository.loadUserGraph(username).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Usuario não encontrado."));

        return Usuario.toResponseDTO(usuario);
    }

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

        usuario = usuarioRepository.save(usuario);

        registrarAuditoria(usuario.getUsername(), "CADASTRO", usuario.getId());

        return "Usuário criado com sucesso!";
    }

    public String update(Long id, UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        this.validarParametrosObrigatorios(usuarioUpdateRequestDTO);

        var usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Usuário não encontrado."));

        this.validarInformacoesUsuario(usuarioUpdateRequestDTO.getUsername(), usuarioUpdateRequestDTO.getEmail());

        usuario.setUsername(usuarioUpdateRequestDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioUpdateRequestDTO.getPassword()));
        usuario.setEmail(usuarioUpdateRequestDTO.getEmail());
        usuario.setDataModificacao(LocalDateTime.now());

        usuario = usuarioRepository.save(usuario);

        registrarAuditoria(usuario.getUsername(), "UPDATE", usuario.getId());

        return "Usuário atualizado com sucesso!";
    }

    public String delete(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Usuário não encontrado."));

        usuario.setStatus(false);
        usuario.setDataModificacao(LocalDateTime.now());

        usuario = usuarioRepository.save(usuario);

        registrarAuditoria(usuario.getUsername(), "DELETE", usuario.getId());

        return "Usuário atualizado com sucesso!";
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        var usuario = usuarioRepository.findById(userId).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Usuário não encontrado."));

        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Role não encontrada."));

        var usuarioRole = UsuarioRole.builder()
                .role(role)
                .usuario(usuario)
                .build();

        usuarioRole = usuarioRoleRepository.save(usuarioRole);

        registrarAuditoria(usuario.getUsername(), "ADD_ROLE_USUARIO", usuarioRole.getRole().getId());
    }

    public void removeRoleFromUser(Long userId, Long roleId) {
        var usuarioRoleId = UsuarioRoleId.builder()
                .roleId(roleId)
                .usuarioId(userId)
                .build();

        usuarioRoleRepository.findById(usuarioRoleId).ifPresent(usuarioRoleRepository::delete);

        registrarAuditoria(String.valueOf(userId), "DELETE_ROLE_USUARIO", roleId);
    }

    private void registrarAuditoria(String usuario, String acao, Long idEntidade) {
        var auditoriaRequestDTO = AuditoriaRequestDTO.builder()
                .usuario(usuario)
                .acao(acao)
                .nomeEntidade("Usuario")
                .idEntidade(String.valueOf(idEntidade))
                .ip("-")
                .detalhes("sucesso")
                .build();

        auditoriaService.save(auditoriaRequestDTO);
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

