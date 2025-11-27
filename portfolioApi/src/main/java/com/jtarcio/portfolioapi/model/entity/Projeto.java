package com.jtarcio.portfolioapi.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jtarcio.portfolioapi.model.entity.enums.ClassificacaoRiscoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_projetos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @NonNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    @NonNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate previsaoFim;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;

    @NonNull
    private BigDecimal orcamentoTotal;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "gerente_id")
    private Membro gerente;

    @Enumerated(EnumType.ORDINAL)
    @NonNull
    private StatusProjetoEnum status;

    @Transient
    private ClassificacaoRiscoEnum classificacaoRiscoEnum;

    @ManyToMany
    @JoinTable(name = "projeto_membros", joinColumns = @JoinColumn(name = "projeto_id"), inverseJoinColumns = @JoinColumn(name = "membro_id"))
    private List<Membro> membros;
}
