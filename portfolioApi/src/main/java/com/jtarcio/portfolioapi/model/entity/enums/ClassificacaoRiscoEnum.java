package com.jtarcio.portfolioapi.model.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClassificacaoRiscoEnum {
    BAIXO_RISCO(1),
    MEDIO_RISCO(2),
    ALTO_RISCO(3);

    private final int codigo;

}
