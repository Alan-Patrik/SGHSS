package com.alanpatrik.sghss.api.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConsultaCriadaEvent {
    private final ConsultaCriadaPayload payload;
}
