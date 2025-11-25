package com.jtarcio.portfolioapi.model.entity;

import com.jtarcio.portfolioapi.model.entity.enums.ClassificacaoRiscoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "tb_projetos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projeto {
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @NonNull
    private LocalDate dataInicio;

    @NonNull
    private LocalDate previsaoFim;

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
