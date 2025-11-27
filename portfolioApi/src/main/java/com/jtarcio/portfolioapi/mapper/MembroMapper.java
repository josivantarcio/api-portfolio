package com.jtarcio.portfolioapi.mapper;

import com.jtarcio.portfolioapi.dto.response.MembroResponseDTO;
import com.jtarcio.portfolioapi.dto.request.MembroRequestDTO;
import com.jtarcio.portfolioapi.model.entity.Membro;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MembroMapper {

    public Membro toEntity(MembroRequestDTO dto) {
        return Membro.builder()
                .nome(dto.getNome())
                .atribuicaoEnum(dto.getAtribuicaoEnum())
                .build();
    }

    public MembroResponseDTO toResponseDTO(Membro membro) {
        return MembroResponseDTO.builder()
                .id(membro.getId())
                .nome(membro.getNome())
                .atribuicaoEnum(membro.getAtribuicaoEnum())
                .build();
    }

    public List<MembroResponseDTO> toResponseDTOList(List<Membro> membros) {
        return membros.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
