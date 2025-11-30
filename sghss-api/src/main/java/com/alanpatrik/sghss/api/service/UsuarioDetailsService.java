package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.repository.UsuarioRepository;
import com.alanpatrik.sghss.api.util.LogonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.loadUserGraph(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        var strings = LogonUtils.extractAuthorityStrings(usuario);
        var granted = LogonUtils.toGrantedAuthorities(strings);

        return User
                .withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(granted)
                .build();
    }
}
