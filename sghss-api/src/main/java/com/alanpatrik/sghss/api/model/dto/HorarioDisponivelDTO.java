package com.alanpatrik.sghss.api.model.dto;

import com.alanpatrik.sghss.api.model.enums.StatusHorario;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HorarioDisponivelDTO {

    @Enumerated(EnumType.STRING)
    private StatusHorario status;
    private LocalDateTime horarioDisponivel;
}
