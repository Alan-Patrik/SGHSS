package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.AuditoriaDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AUDIT_LOG",
        indexes = {
                @Index(name = "idx_audit_event_time", columnList = "DAT_EVENT_TIME"),
                @Index(name = "idx_audit_username", columnList = "TXT_USERNAME"),
                @Index(name = "idx_audit_action", columnList = "TXT_ACTION"),
                @Index(name = "idx_audit_resource", columnList = "TXT_RESOURCE")
        })
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AUDITORIA")
    private Long id;

    @Column(name = "DAT_EVENT_TIME", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "TXT_USERNAME", length = 100)
    private String username;

    @Column(name = "TXT_ACTION", length = 150)
    private String action;

    @Column(name = "TXT_RESOURCE", length = 300)
    private String resource;

    @Column(name = "TXT_METHOD", length = 20)
    private String httpMethod;

    @Column(name = "TXT_IP", length = 64)
    private String ip;

    @Column(name = "TXT_USER_AGENT", length = 512)
    private String userAgent;

    @Column(name = "TXT_DETAILS", columnDefinition = "CLOB")
    private String details;

    public static AuditoriaDTO toDTO(Auditoria auditoria) {
        var auditoriaDTO = new AuditoriaDTO();
        auditoriaDTO.setId(auditoria.getId());
        auditoriaDTO.setEventTime(auditoria.getEventTime());
        auditoriaDTO.setUsername(auditoria.getUsername());
        auditoriaDTO.setAction(auditoria.getAction());
        auditoriaDTO.setResource(auditoria.getResource());
        auditoriaDTO.setHttpMethod(auditoria.getHttpMethod());
        auditoriaDTO.setIp(auditoria.getIp());
        auditoriaDTO.setUserAgent(auditoria.getUserAgent());
        auditoriaDTO.setDetails(auditoria.getDetails());
        return auditoriaDTO;
    }

    @PrePersist
    public void prePersist() {
        if (eventTime == null) eventTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Auditoria auditoria = (Auditoria) object;
        return Objects.equals(id, auditoria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
