package com.jtarcio.portfolioapi.model.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Atribuicao {

        GERENTE(1),
        FUNCIONARIO(2);

        private final int codigo;
}
