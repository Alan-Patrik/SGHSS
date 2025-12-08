package com.alanpatrik.sghss.api.notifications;

import com.alanpatrik.sghss.api.events.ConsultaCriadaPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class EmailNotificationService {

    private final MailProvider mailProvider;
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;

    @Value("${mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${mail.from:noreply@sghss.com}")
    private String from;

    public void notificarConsultaCriada(ConsultaCriadaPayload payload) {
        var locale = new Locale("pt", "BR");
        var dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", locale);

        String subject = messageSource.getMessage(
                "email.consultaCriada.subject",
                new Object[]{dateTimeFormatter.format(payload.getDataHora())},
                "Consulta agendada - " + dateTimeFormatter.format(payload.getDataHora()),
                locale
        );

        var context = new Context(locale);
        context.setVariable("nome", payload.getNomePaciente());
        context.setVariable("dataHora", payload.getDataHora());
        context.setVariable("especialidade", payload.getEspecialidade());
        context.setVariable("tipoConsulta", payload.getTipoConsulta());
        context.setVariable("projeto", "SGHSS");

        String html = templateEngine.process("email/consulta-criada", context);
        mailProvider.sendHtml(from, payload.getEmail(), subject, html);
    }

    public void notificarConsultaOnline(ConsultaCriadaPayload payload) {
        if (!mailEnabled) return;

        var locale = new Locale("pt", "BR");
        var dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", locale);

        String subject = messageSource.getMessage(
                "email.consultaOnline.subject",
                new Object[]{dateTimeFormatter.format(payload.getDataHora())},
                "Consulta online - " + dateTimeFormatter.format(payload.getDataHora()),
                locale
        );

        var context = new Context(locale);
        context.setVariable("nome", payload.getNomePaciente());
        context.setVariable("dataHora", payload.getDataHora());
        context.setVariable("especialidade", payload.getEspecialidade());
        context.setVariable("tipoConsulta", payload.getTipoConsulta());
        context.setVariable("meetingUrl", payload.getMeetingUrl());
        context.setVariable("expiraEm", payload.getExpiraEm());
        context.setVariable("projeto", "SGHSS");

        String html = templateEngine.process("email/consulta-online", context);
        mailProvider.sendHtml(from, payload.getEmail(), subject, html);
    }
}

