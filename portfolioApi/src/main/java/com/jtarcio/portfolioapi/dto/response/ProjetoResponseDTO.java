package com.jtarcio.portfolioapi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jtarcio.portfolioapi.model.entity.enums.ClassificacaoRiscoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoResponseDTO {

    private Long id;
    private String nome;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate previsaoFim;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;

    private BigDecimal orcamentoTotal;
    private String descricao;
    private ClassificacaoRiscoEnum classificacaoRiscoEnum;
    private StatusProjetoEnum status;
    private MembroResponseDTO gerente;
    private List<MembroResponseDTO> membros;
}
