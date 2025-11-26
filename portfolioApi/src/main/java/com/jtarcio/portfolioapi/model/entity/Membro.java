package com.jtarcio.portfolioapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Entity
@Table(name = "tb_membros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @Enumerated(EnumType.STRING)
    @NonNull
    private AtribuicaoEnum atribuicaoEnum;

    @ManyToMany(mappedBy = "membros")
    @JsonIgnore
    private List<Projeto> projeto;
}
