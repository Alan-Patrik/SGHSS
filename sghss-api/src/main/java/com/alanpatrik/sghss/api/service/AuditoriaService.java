package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.model.Auditoria;
import com.alanpatrik.sghss.api.model.dto.request.AuditoriaRequestDTO;
import com.alanpatrik.sghss.api.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public void save(AuditoriaRequestDTO auditoriaRequestDTO) {
        var auditoria = Auditoria.builder()
                .usuario(auditoriaRequestDTO.getUsuario())
                .acao(auditoriaRequestDTO.getAcao())
                .nomeEntidade(auditoriaRequestDTO.getNomeEntidade())
                .idEntidade(auditoriaRequestDTO.getIdEntidade())
                .ip(auditoriaRequestDTO.getIp())
                .dataHora(LocalDateTime.now())
                .detalhes(auditoriaRequestDTO.getDetalhes())
                .build();

        auditoriaRepository.save(auditoria);
    }
}
