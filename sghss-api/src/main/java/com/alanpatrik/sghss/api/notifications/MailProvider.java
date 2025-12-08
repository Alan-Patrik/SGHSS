package com.alanpatrik.sghss.api.notifications;

public interface MailProvider {
    void sendHtml(String from, String to, String subject, String html);
}