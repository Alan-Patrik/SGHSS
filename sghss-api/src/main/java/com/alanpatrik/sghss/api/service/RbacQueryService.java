package com.alanpatrik.sghss.api.service;


import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.model.dto.response.PrivilegioResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.alanpatrik.sghss.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RbacQueryService {

    private final UsuarioRepository usuarioRepository;

    public Set<PrivilegioResponseDTO> getPrivilegesOfUser(String username) {
        var usuario = usuarioRepository.loadUserGraph(username).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Usuário não encontrado: " + username));

        return usuario.getUsuarioRoles().stream()
                .flatMap(ur -> ur.getRole().getRolePrivilegios().stream())
                .map(rp -> new PrivilegioResponseDTO(rp.getPrivilegio().getId(), rp.getPrivilegio().getName()))
                .collect(toCollection(HashSet::new));
    }

    public Set<RoleResponseDTO> getRolesOfUser(String username) {
        var usuario = usuarioRepository.loadUserGraph(username).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Usuário não encontrado: " + username));

        return usuario.getUsuarioRoles().stream()
                .map(ur -> new RoleResponseDTO(ur.getRole().getId(), ur.getRole().getAuthority(),
                        ur.getRole().getRolePrivilegios().stream()
                                .map(rp -> rp.getPrivilegio().getName())
                                .collect(toSet())))
                .collect(toSet());
    }
}

