package com.jtarcio.portfolioapi.service;

import com.jtarcio.portfolioapi.exception.PortfolioException;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    //buscar por todos os membro
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
        membroRepository.deleteById(id);
    }

    //limite de 3 projetos
    public boolean newProjeto(Membro membro) {
        long projetoAtivo = membro.getProjeto()
                .stream()
                .filter(p -> p.getStatus().isStatusFianlizado() != true)
                .count();
        return projetoAtivo < 3;
    }

    //buscar membro mock
    public List<Membro> buscarMembroMock() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/mock/membros";
        Membro[] membro = restTemplate.getForObject(url, Membro[].class);
        assert membro != null;
        return List.of(membro);
    }
}
