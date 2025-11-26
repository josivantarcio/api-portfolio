package com.jtarcio.portfolioapi.service;

import com.jtarcio.portfolioapi.exception.PortfolioException;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MembroService {

    private final MembroRepository membroRepository;

    @Autowired
    public MembroService(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
    }

    //buscar todos os membros
    public List<Membro> findAll() {
        return membroRepository.findAll();
    }

    //buscar por id
    public Membro findById(Long id) {
        return membroRepository.findById(id)
                .orElseThrow(() -> new PortfolioException("Membro não foi localizado no banco de dados"));
    }

    //salvar membro
    public Membro save(Membro membro) {
        return membroRepository.save(membro);
    }

    //deletar membro
    public void delete(Long id) {
        Membro membro = findById(id);
        membroRepository.delete(membro);
    }

    //validar se membro pode ser alocado em novo projeto (limite de 3 projetos ativos)
    public boolean podeAlocarEmNovoProjeto(Membro membro) {
        long projetosAtivos = membro.getProjetos()
                .stream()
                .filter(p -> !p.getStatus().isStatusFinalizado())
                .count();
        return projetosAtivos < 3;
    }

    //buscar membros da API mock externa
    public List<Membro> buscarMembroMock() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/mock/membros";

        ResponseEntity<List<Membro>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Membro>>() {
                }
        );

        return response.getBody();
    }
}
