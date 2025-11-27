package com.jtarcio.portfolioapi.dto.response;

import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembroResponseDTO {

    private Long id;
    private String nome;
    private AtribuicaoEnum atribuicaoEnum;
}
