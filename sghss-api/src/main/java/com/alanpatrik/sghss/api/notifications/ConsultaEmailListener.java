package com.alanpatrik.sghss.api.notifications;

import com.alanpatrik.sghss.api.events.ConsultaCriadaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class ConsultaEmailListener {

    private final EmailNotificationService emailNotificationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onConsultaCriada(ConsultaCriadaEvent event) {
        emailNotificationService.notificarConsultaCriada(event.getPayload());
    }
}
