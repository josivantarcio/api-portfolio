package com.jtarcio.portfolioapi.model.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClassificacaoRisco {
    BAIXO(1),
    MEDIO(2),
    ALTO(3);

    private final int codigo;

}
