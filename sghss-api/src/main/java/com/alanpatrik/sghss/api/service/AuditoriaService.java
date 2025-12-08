package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Auditoria;
import com.alanpatrik.sghss.api.model.dto.AuditoriaDTO;
import com.alanpatrik.sghss.api.model.dto.request.AuditoriaRequestDTO;
import com.alanpatrik.sghss.api.repository.AuditoriaRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public Page<AuditoriaDTO> search(String username, String action, String resource,
                                     LocalDateTime from, LocalDateTime to,
                                     int page, int size, String sort) {
        Pageable pageable = toPageable(page, size, sort);
        var result = auditoriaRepository.search(username, action, resource, from, to, pageable);
        return result.map(Auditoria::toDTO);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public AuditoriaDTO findOne(Long id) {
        var auditoria = auditoriaRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.AUDITORIA_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))));

        return Auditoria.toDTO(auditoria);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public String exportCsv(String username, String action, String resource,
                            LocalDateTime from, LocalDateTime to, String sort) {
        Pageable pageable = toPageable(0, Integer.MAX_VALUE, sort);
        var page = auditoriaRepository.search(username, action, resource, from, to, pageable);

        var header = "id;eventTime;username;action;resource;httpMethod;ip;userAgent;details";
        var lines = page.getContent().stream()
                .map(a -> String.join(";",
                        safe(a.getId()),
                        safe(a.getEventTime()),
                        safe(a.getUsername()),
                        safe(a.getAction()),
                        safe(a.getResource()),
                        safe(a.getHttpMethod()),
                        safe(a.getIp()),
                        safe(a.getUserAgent()),
                        safe(a.getDetails())
                ))
                .collect(Collectors.toList());

        return header + "\n" + String.join("\n", lines);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AuditoriaDTO create(AuditoriaRequestDTO auditoriaRequestDTO) {
        this.validarParametrosObrigatorios(auditoriaRequestDTO);

        var auditoria = new Auditoria();
        auditoria.setEventTime(LocalDateTime.now());
        auditoria.setUsername(emptyToNull(auditoriaRequestDTO.getUsername()));
        auditoria.setAction(emptyToNull(auditoriaRequestDTO.getAction()));
        auditoria.setResource(emptyToNull(auditoriaRequestDTO.getResource()));
        auditoria.setIp(emptyToNull(auditoriaRequestDTO.getIp()));
        auditoria.setUserAgent(emptyToNull(auditoriaRequestDTO.getUserAgent()));
        auditoria.setDetails(emptyToNull(auditoriaRequestDTO.getDetails()));
        auditoria.setHttpMethod(emptyToNull(auditoriaRequestDTO.getHttpMethod()));

        auditoria = auditoriaRepository.save(auditoria);
        return Auditoria.toDTO(auditoria);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public List<AuditoriaDTO> recent(int limit) {
        if (limit <= 0) limit = 10;
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.desc("eventTime")));
        return auditoriaRepository.findTopN(pageable).stream().map(Auditoria::toDTO).toList();
    }

    private Pageable toPageable(int page, int size, String sort) {
        if (!StringUtils.hasText(sort)) {
            return PageRequest.of(page, size, Sort.by(Sort.Order.desc("eventTime")));
        }
        // sort ex.: "eventTime,desc" ou "username,asc"
        String[] parts = sort.split(",");
        var prop = parts[0].trim();
        var asc = parts.length < 2 || !"desc".equalsIgnoreCase(parts[1].trim());
        return PageRequest.of(page, size, asc ? Sort.by(prop).ascending() : Sort.by(prop).descending());
    }

    private String safe(Object object) {
        if (object == null) return "";
        String string = String.valueOf(object);
        // Normaliza para CSV; troca quebras de linha
        string = string.replace("\r", " ").replace("\n", " ");
        // opcional: remover ';'
        return string;
    }

    private String emptyToNull(String string) {
        return (string == null || string.isBlank()) ? null : string;
    }

    private void validarParametrosObrigatorios(AuditoriaRequestDTO auditoriaRequestDTO) {
        if (auditoriaRequestDTO.getUsername() == null ||
                auditoriaRequestDTO.getUsername().isEmpty() ||
                auditoriaRequestDTO.getUsername().isBlank()) {
            throw new ParametroInvalidoException("O campo Username é obrigatório.");
        }
        if (auditoriaRequestDTO.getAction() == null ||
                auditoriaRequestDTO.getAction().isEmpty() ||
                auditoriaRequestDTO.getAction().isBlank()) {
            throw new ParametroInvalidoException("O campo Action é obrigatório.");
        }
        if (auditoriaRequestDTO.getResource() == null ||
                auditoriaRequestDTO.getResource().isEmpty() ||
                auditoriaRequestDTO.getResource().isBlank()) {
            throw new ParametroInvalidoException("O campo Resource é obrigatório.");
        }
        if (auditoriaRequestDTO.getIp() == null ||
                auditoriaRequestDTO.getIp().isEmpty() ||
                auditoriaRequestDTO.getIp().isBlank()) {
            throw new ParametroInvalidoException("O campo Ip é obrigatório.");
        }
        if (auditoriaRequestDTO.getUserAgent() == null ||
                auditoriaRequestDTO.getUserAgent().isEmpty() ||
                auditoriaRequestDTO.getUserAgent().isBlank()) {
            throw new ParametroInvalidoException("O campo UserAgent é obrigatório.");
        }
        if (auditoriaRequestDTO.getDetails() == null ||
                auditoriaRequestDTO.getDetails().isEmpty() ||
                auditoriaRequestDTO.getDetails().isBlank()) {
            throw new ParametroInvalidoException("O campo Details é obrigatório.");
        }
        if (auditoriaRequestDTO.getHttpMethod() == null ||
                auditoriaRequestDTO.getHttpMethod().isEmpty() ||
                auditoriaRequestDTO.getHttpMethod().isBlank()) {
            throw new ParametroInvalidoException("O campo HttpMethod é obrigatório.");
        }
    }
}
