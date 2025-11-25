package com.jtarcio.portfolioapi.model.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusProjeto {
    /*
     * em análise →
     * análise realizada →
     * análise aprovada →
     * iniciado →
     * planejado →
     * em andamento →
     * encerrado
     * cancelado
     * */

    EM_ANALISE(0),
    ANALISE_REALIZADA(1),
    ANALISE_APROVADA(2),
    INICIADO(3),
    PLANEJADO(4),
    EM_ANDAMENTO(5),
    ENCERRADO(6),
    CANCELADO(7);

    private final int value;
}
