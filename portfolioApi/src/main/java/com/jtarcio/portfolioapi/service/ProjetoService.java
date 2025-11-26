package com.jtarcio.portfolioapi.service;

import com.jtarcio.portfolioapi.exception.PortfolioException;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.model.entity.Projeto;
import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.ClassificacaoRiscoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import com.jtarcio.portfolioapi.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final MembroService membroService;

    @Autowired
    public ProjetoService(ProjetoRepository projetoRepository, MembroService membroService) {
        this.projetoRepository = projetoRepository;
        this.membroService = membroService;
    }

    //buscar todos os projetos com paginação
    @Transactional(readOnly = true)
    public Page<Projeto> findAll(Pageable pageable) {
        Page<Projeto> projetos = projetoRepository.findAll(pageable);
        projetos.forEach(p -> p.getMembros().size());
        return projetos;
    }

    //buscar todos os projetos (sem paginação)
    @Transactional(readOnly = true)
    public List<Projeto> findAll() {
        List<Projeto> projetos = projetoRepository.findAll();
        projetos.forEach(p -> p.getMembros().size());
        return projetos;
    }

    //projeto por id
    @Transactional(readOnly = true)
    public Projeto findById(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new PortfolioException("Não encontramos esse ID: " + id));
        projeto.getMembros().size();
        return projeto;
    }


    //criar novo projeto com validações
    @Transactional
    public Projeto create(Projeto projeto) {
        validarCamposObrigatorios(projeto);
        validarDatas(projeto);
        validarGerente(projeto);
        validarMembros(projeto);

        projeto.setClassificacaoRiscoEnum(calcularClassificacaoRisco(projeto));

        return projetoRepository.save(projeto);
    }

    //atualizar projeto existente
    @Transactional
    public Projeto update(Long id, Projeto projetoAtualizado) {
        Projeto projetoExistente = findById(id);

        validarCamposObrigatorios(projetoAtualizado);
        validarDatas(projetoAtualizado);
        validarGerente(projetoAtualizado);
        validarMembros(projetoAtualizado);

        projetoExistente.setNome(projetoAtualizado.getNome());
        projetoExistente.setDataInicio(projetoAtualizado.getDataInicio());
        projetoExistente.setPrevisaoFim(projetoAtualizado.getPrevisaoFim());
        projetoExistente.setDataFim(projetoAtualizado.getDataFim());
        projetoExistente.setOrcamentoTotal(projetoAtualizado.getOrcamentoTotal());
        projetoExistente.setDescricao(projetoAtualizado.getDescricao());
        projetoExistente.setGerente(projetoAtualizado.getGerente());
        projetoExistente.setMembros(projetoAtualizado.getMembros());

        projetoExistente.setClassificacaoRiscoEnum(calcularClassificacaoRisco(projetoExistente));

        return projetoRepository.save(projetoExistente);
    }

    //deleta projeto com validação de status
    @Transactional
    public void delete(Long id) {
        Projeto projeto = findById(id);

        //analisa se pode excluir
        if (projeto.getStatus() == StatusProjetoEnum.INICIADO || projeto.getStatus() == StatusProjetoEnum.EM_ANDAMENTO
                || projeto.getStatus() == StatusProjetoEnum.ENCERRADO) {
            throw new PortfolioException(
                    "Não é possível excluir projeto com status: " + projeto.getStatus()
            );
        }
        projetoRepository.delete(projeto);
    }

    /**
     * Calcular classificação de risco
     * Regras:
     * - BAIXO: orçamento ≤ R$ 100.000 E prazo ≤ 3 meses
     * - MÉDIO: orçamento entre R$ 100.001 e R$ 500.000 OU prazo entre 3 a 6 meses
     * - ALTO: orçamento > R$ 500.000 OU prazo > 6 meses
     */
    public ClassificacaoRiscoEnum calcularClassificacaoRisco(Projeto projeto) {
        BigDecimal orcamento = projeto.getOrcamentoTotal();
        long meses = ChronoUnit.MONTHS.between(
                projeto.getDataInicio(),
                projeto.getPrevisaoFim()
        );

        //orçamento até 100k E prazo até 3 meses
        if (orcamento.compareTo(new BigDecimal("100000")) <= 0 && meses <= 3) {
            return ClassificacaoRiscoEnum.BAIXO_RISCO;
        }

        //orçamento acima de 500k OU prazo maior que 6 meses
        if (orcamento.compareTo(new BigDecimal("500000")) > 0 || meses > 6) {
            return ClassificacaoRiscoEnum.ALTO_RISCO;
        }

        //casos intermediários
        return ClassificacaoRiscoEnum.MEDIO_RISCO;
    }

    //alterar status do projeto (com validação de transição sequencial)
    @Transactional
    public Projeto alterarStatus(Long id, StatusProjetoEnum novoStatus) {
        Projeto projeto = findById(id);
        StatusProjetoEnum statusAtual = projeto.getStatus();

        //a qualquer momento pode cancelar
        if (novoStatus == StatusProjetoEnum.CANCELADO) {
            projeto.setStatus(novoStatus);
            return projetoRepository.save(projeto);
        }

        //validar transição sequencial
        if (!statusAtual.possoMudarStatus(novoStatus)) {
            throw new PortfolioException(
                    String.format("Transição inválida de %s para %s. Deve seguir a sequência. :)",
                            statusAtual, novoStatus)
            );
        }

        projeto.setStatus(novoStatus);

        //registrar data de término se terminar
        if (novoStatus == StatusProjetoEnum.ENCERRADO) {
            projeto.setDataFim(LocalDate.now());
        }

        return projetoRepository.save(projeto);
    }

    //avançar para próximo status na sequência
    @Transactional
    public Projeto avancarProximoStatus(Long id) {
        Projeto projeto = findById(id);
        StatusProjetoEnum proximoStatus = projeto.getStatus().proximoStatus();
        return alterarStatus(id, proximoStatus);
    }

    //adicionar membro ao projeto
    @Transactional
    public Projeto adicionarMembro(Long projetoId, Long membroId) {
        Projeto projeto = findById(projetoId);
        Membro membro = membroService.findById(membroId);

        //validar se já está na lista
        if (projeto.getMembros().contains(membro)) {
            throw new PortfolioException("Membro já está alocado neste projeto");
        }

        //limite de 10 funcionarios no projeto
        if (projeto.getMembros().size() >= 10) {
            throw new PortfolioException("Projeto já possui o máximo de 10 pessoas alocadas!");
        }


        if (membro.getAtribuicaoEnum() != AtribuicaoEnum.FUNCIONARIO) {
            throw new PortfolioException("Apenas membros com atribuição FUNCIONARIO podem ser associados");
        }

        //3 projetos ativos por membro
        if (!membroService.podeAlocarEmNovoProjeto(membro)) {
            throw new PortfolioException("Membro já está alocado em 3 projetos ativos");
        }

        projeto.getMembros().add(membro);
        return projetoRepository.save(projeto);
    }

    //remover membro de prjeto
    @Transactional
    public Projeto removerMembro(Long projetoId, Long membroId) {
        Projeto projeto = findById(projetoId);
        Membro membro = membroService.findById(membroId);


        if (projeto.getMembros().size() <= 1) {
            throw new PortfolioException("Projeto deve ter pelo menos 1 membro");
        }

        projeto.getMembros().remove(membro);
        return projetoRepository.save(projeto);
    }

    //gerar relatório resumido do portfólio
    @Transactional(readOnly = true)
    public Map<String, Object> gerarRelatorioPortfolio() {
        List<Projeto> todosProjetos = projetoRepository.findAll();

        todosProjetos.forEach(p -> p.getMembros().size());

        Map<String, Object> relatorio = new HashMap<>();

        // • Quantidade de projetos por status
        Map<StatusProjetoEnum, Long> projetosPorStatus = todosProjetos
                .stream()
                .collect(Collectors
                        .groupingBy(Projeto::getStatus, Collectors.counting()));

        relatorio.put("Quantidade por Status", projetosPorStatus);


        // • Total orçado por status
        Map<StatusProjetoEnum, BigDecimal> orcamentoPorStatus = todosProjetos
                .stream()
                .collect(Collectors.groupingBy(Projeto::getStatus, Collectors.reducing(
                                BigDecimal.ZERO,
                                Projeto::getOrcamentoTotal,
                                BigDecimal::add
                        )
                ));

        relatorio.put("Total orçado por status", orcamentoPorStatus);


        // • Média de duração dos projetos encerrados
        List<Projeto> projetosEncerrados = todosProjetos
                .stream()
                .filter(p -> p.getStatus() == StatusProjetoEnum.ENCERRADO && p.getDataFim() != null)
                .toList();

        if (!projetosEncerrados.isEmpty()) {
            double mediaDias = projetosEncerrados
                    .stream()
                    .mapToLong(p -> ChronoUnit.DAYS.between(p.getDataInicio(), p.getDataFim()))
                    .average()
                    .orElse(0.00);
            relatorio.put("Media da duracao dos projetos finalizados", Math.round(mediaDias) + " dias");
        } else {
            relatorio.put("Media duracao projetos finalziado", "Nenhum projeto finalizado");
        }

        // • Total de membros únicos alocados
        Set<Membro> membrosUnicos = todosProjetos
                .stream()
                .flatMap(p -> p.getMembros().stream())
                .collect(Collectors.toSet());

        relatorio.put("Total de membros únicos é:", membrosUnicos.size());

        return relatorio;
    }

    private void validarCamposObrigatorios(Projeto projeto) {
        if (projeto.getNome().isBlank()) {
            throw new PortfolioException("Nome do projeto é obrigatório");
        }

        if (projeto.getDataInicio() == null) {
            throw new PortfolioException("Data de início é obrigatória");
        }

        if (projeto.getPrevisaoFim() == null) {
            throw new PortfolioException("Previsão de término é obrigatória");
        }


        if (projeto.getOrcamentoTotal() == null || projeto.getOrcamentoTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PortfolioException("Orçamento total deve ser maior que zero");
        }

        if (projeto.getStatus() == null) {
            throw new PortfolioException("Status é obrigatório");
        }
    }

    private void validarDatas(Projeto projeto) {
        if (projeto.getPrevisaoFim().isBefore(projeto.getDataInicio())) {
            throw new PortfolioException("Data de previsão de término não pode ser anterior à data de início");
        }
        if (projeto.getDataFim() != null && projeto.getDataFim().isBefore(projeto.getDataInicio())) {
            throw new PortfolioException("Data de término não pode ser anterior à data de início");
        }
    }

    private void validarGerente(Projeto projeto) {
        if (projeto.getGerente() == null) {
            throw new PortfolioException("Gerente responsável é obrigatório");
        }
    }

    private void validarMembros(Projeto projeto) {
        if (projeto.getMembros() == null || projeto.getMembros().isEmpty()) {
            throw new PortfolioException("Projeto deve ter pelo menos 1 membro");
        }
        if (projeto.getMembros().size() > 10) {
            throw new PortfolioException("Projeto pode ter no máximo 10 membros");
        }

        // Validar
        for (Membro membro : projeto.getMembros()) {
            Membro membroCompleto = membroService.findById(membro.getId());
            if (membroCompleto.getAtribuicaoEnum() != AtribuicaoEnum.FUNCIONARIO) {
                throw new PortfolioException("Apenas membros com atribuição FUNCIONARIO podem ser associados");
            }
        }
    }

}
