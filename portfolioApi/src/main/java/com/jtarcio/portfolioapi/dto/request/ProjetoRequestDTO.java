package com.jtarcio.portfolioapi.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class ProjetoRequestDTO {

    @NotBlank(message = "Precisa ter o campo nome!")
    private String nome;

    @NotNull(message = "Data de início não pode ficar em branco")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    @NotNull(message = "Previsão de término não pode ficar em branco")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate previsaoFim;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;

    @NotNull(message = "Orçamento total não Poderá ficar em branco. Favor preencher")
    @Positive(message = "Orçamento deve ser maior que zero e jamais negativo")
    private BigDecimal orcamentoTotal;

    private String descricao;

    @NotNull(message = "Campo Gerente é obrigatório!!!")
    private Long gerenteId;

    @NotNull(message = "Status é obrigatório")
    private StatusProjetoEnum status;

    @NotNull(message = "Lista de membros é obrigatória. Minimo 1 e máximo 10")
    private List<Long> membrosIds;
}
