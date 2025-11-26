package com.jtarcio.portfolioapi.controller.mock;

import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mock/membros")
@Tag(name = "Mock API", description = "Simulação de API externa de membros")
public class MembroMockController {

    @GetMapping
    @Operation(summary = "Listar membros mock", description = "Retorna lista simulada de membros")
    public List<Membro> listarMembrosMock() {
        List<Membro> membros = new ArrayList<>();
        membros.add(new Membro(1L, "Josevan Oliveira", AtribuicaoEnum.FUNCIONARIO, new ArrayList<>()));
        membros.add(new Membro(2L, "branca Oliveira", AtribuicaoEnum.FUNCIONARIO, new ArrayList<>()));
        membros.add(new Membro(3L, "Bruno Felipe", AtribuicaoEnum.TERCERIZADO, new ArrayList<>()));
        membros.add(new Membro(4L, "Rebeca Loren", AtribuicaoEnum.ACIONISTA, new ArrayList<>()));
        return membros;
    }
}
