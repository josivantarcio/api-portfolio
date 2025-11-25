package com.jtarcio.portfolioapi.model.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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

    private final int codigo;

    public boolean possoMudarStatus(StatusProjeto proximoStatus) {
        return proximoStatus.getCodigo() == this.codigo + 1;
    }

    public StatusProjeto proximoStatus() {
        if (this == CANCELADO || this == ENCERRADO) {
            throw new IllegalArgumentException("Último Status.");
        } else {
            return StatusProjeto.values()[this.codigo + 1]; //vai usar o indice
        }
    }

    // bloquear a exclusão de projetos finalizados
    public boolean isStatusFianlizado() {
        return this == ENCERRADO || this == CANCELADO;
    }

    public static StatusProjeto codigo(int codigo) {
        return Arrays.stream(values())
                .filter(s -> s.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Codigo de Status Invalido: " + codigo));
    }

}
