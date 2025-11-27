package com.jtarcio.portfolioapi.dto.request;

import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembroRequestDTO {

    @NotBlank(message = "Precisa ter o campo nome!")
    private String nome;

    @NotNull
    private AtribuicaoEnum atribuicaoEnum;
}
