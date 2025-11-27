package com.jtarcio.portfolioapi.mapper;

import com.jtarcio.portfolioapi.dto.request.ProjetoRequestDTO;
import com.jtarcio.portfolioapi.dto.response.ProjetoResponseDTO;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.model.entity.Projeto;
import com.jtarcio.portfolioapi.service.MembroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProjetoMapper {

    private final MembroService membroService;
    private final MembroMapper membroMapper;

    public Projeto toEntity(ProjetoRequestDTO dto) {
        Membro gerente = membroService.findById(dto.getGerenteId());

        List<Membro> membros = dto.getMembrosIds().stream()
                .map(membroService::findById)
                .collect(Collectors.toList());

        return Projeto.builder()
                .nome(dto.getNome())
                .dataInicio(dto.getDataInicio())
                .previsaoFim(dto.getPrevisaoFim())
                .dataFim(dto.getDataFim())
                .orcamentoTotal(dto.getOrcamentoTotal())
                .descricao(dto.getDescricao())
                .gerente(gerente)
                .status(dto.getStatus())
                .membros(membros)
                .build();
    }

    public ProjetoResponseDTO toResponseDTO(Projeto projeto) {
        return ProjetoResponseDTO.builder()
                .id(projeto.getId())
                .nome(projeto.getNome())
                .dataInicio(projeto.getDataInicio())
                .previsaoFim(projeto.getPrevisaoFim())
                .dataFim(projeto.getDataFim())
                .orcamentoTotal(projeto.getOrcamentoTotal())
                .descricao(projeto.getDescricao())
                .classificacaoRiscoEnum(projeto.getClassificacaoRiscoEnum())
                .status(projeto.getStatus())
                .gerente(membroMapper.toResponseDTO(projeto.getGerente()))
                .membros(membroMapper.toResponseDTOList(projeto.getMembros()))
                .build();
    }

    public List<ProjetoResponseDTO> toResponseDTOList(List<Projeto> projetos) {
        return projetos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
