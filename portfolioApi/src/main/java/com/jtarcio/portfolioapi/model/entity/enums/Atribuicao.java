package com.jtarcio.portfolioapi.model.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Atribuicao {

        TERCERIZADO(1),
        FUNCIONARIO(2),
        ACIONISTA(3);

        private final int codigo;
}
