package com.jtarcio.portfolioapi.model.entity;

import com.jtarcio.portfolioapi.model.entity.enums.Atribuicao;
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
    private Atribuicao atribuicao;

    @ManyToMany(mappedBy = "membros")
    private List<Projeto> projeto;
}
