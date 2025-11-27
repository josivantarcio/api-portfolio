package com.jtarcio.portfolioapi.service;

import com.jtarcio.portfolioapi.exception.PortfolioException;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.model.entity.Projeto;
import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.ClassificacaoRiscoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import com.jtarcio.portfolioapi.repository.ProjetoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ProjetoService")
class ProjetoServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private MembroService membroService;

    @InjectMocks
    private ProjetoService projetoService;

    private Projeto projeto;
    private Membro membro;

    @BeforeEach
    void setUp() {
        membro = Membro.builder()
                .id(1L)
                .nome("João Silva")
                .atribuicaoEnum(AtribuicaoEnum.FUNCIONARIO)
                .build();

        projeto = Projeto.builder()
                .id(1L)
                .nome("Projeto Teste")
                .dataInicio(LocalDate.now())
                .previsaoFim(LocalDate.now().plusMonths(2))
                .orcamentoTotal(new BigDecimal("50000"))
                .descricao("Descrição teste")
                .gerente(membro)
                .status(StatusProjetoEnum.EM_ANALISE)
                .membros(new ArrayList<>(List.of(membro)))
                .build();
    }

    @Test
    @DisplayName("Deve buscar projeto por ID com sucesso")
    void deveBuscarProjetoPorId() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        Projeto resultado = projetoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Projeto Teste", resultado.getNome());
        verify(projetoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar projeto inexistente")
    void deveLancarExcecaoAoBuscarProjetoInexistente() {
        when(projetoRepository.findById(999L)).thenReturn(Optional.empty());

        PortfolioException exception = assertThrows(
                PortfolioException.class,
                () -> projetoService.findById(999L)
        );

        assertEquals("Não encontramos esse ID: 999", exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar todos os projetos com paginação")
    void deveListarTodosProjetosComPaginacao() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Projeto> page = new PageImpl<>(List.of(projeto));

        when(projetoRepository.findAll(pageable)).thenReturn(page);

        Page<Projeto> resultado = projetoService.findAll(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(projetoRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve criar projeto com sucesso")
    void deveCriarProjetoComSucesso() {
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        Projeto resultado = projetoService.create(projeto);

        assertNotNull(resultado);
        assertEquals("Projeto Teste", resultado.getNome());
        verify(projetoRepository, times(1)).save(any(Projeto.class));
    }

    @Test
    @DisplayName("Deve calcular risco BAIXO corretamente")
    void deveCalcularRiscoBaixo() {
        projeto.setOrcamentoTotal(new BigDecimal("80000"));
        projeto.setPrevisaoFim(LocalDate.now().plusMonths(2));

        ClassificacaoRiscoEnum risco = projetoService.calcularClassificacaoRisco(projeto);

        assertEquals(ClassificacaoRiscoEnum.BAIXO_RISCO, risco);
    }

    @Test
    @DisplayName("Deve calcular risco MEDIO corretamente")
    void deveCalcularRiscoMedio() {
        projeto.setOrcamentoTotal(new BigDecimal("250000"));
        projeto.setPrevisaoFim(LocalDate.now().plusMonths(4));

        ClassificacaoRiscoEnum risco = projetoService.calcularClassificacaoRisco(projeto);

        assertEquals(ClassificacaoRiscoEnum.MEDIO_RISCO, risco);
    }

    @Test
    @DisplayName("Deve calcular risco ALTO corretamente")
    void deveCalcularRiscoAlto() {
        projeto.setOrcamentoTotal(new BigDecimal("600000"));
        projeto.setPrevisaoFim(LocalDate.now().plusMonths(8));

        ClassificacaoRiscoEnum risco = projetoService.calcularClassificacaoRisco(projeto);

        assertEquals(ClassificacaoRiscoEnum.ALTO_RISCO, risco);
    }

    @Test
    @DisplayName("Deve alterar status com sucesso")
    void deveAlterarStatusComSucesso() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        Projeto resultado = projetoService.alterarStatus(1L, StatusProjetoEnum.ANALISE_REALIZADA);

        assertNotNull(resultado);
        assertEquals(StatusProjetoEnum.ANALISE_REALIZADA, resultado.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar projeto com status INICIADO")
    void deveLancarExcecaoAoDeletarProjetoIniciado() {
        projeto.setStatus(StatusProjetoEnum.INICIADO);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        PortfolioException exception = assertThrows(
                PortfolioException.class,
                () -> projetoService.delete(1L)
        );

        assertTrue(exception.getMessage().contains("Não é possível excluir"));
    }

    @Test
    @DisplayName("Deve adicionar membro ao projeto com sucesso")
    void deveAdicionarMembroAoProjeto() {
        Membro novoMembro = Membro.builder()
                .id(2L)
                .nome("Maria Santos")
                .atribuicaoEnum(AtribuicaoEnum.FUNCIONARIO)
                .build();

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroService.findById(2L)).thenReturn(novoMembro);
        when(membroService.podeAlocarEmNovoProjeto(novoMembro)).thenReturn(true);
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        Projeto resultado = projetoService.adicionarMembro(1L, 2L);

        assertNotNull(resultado);
        verify(projetoRepository, times(1)).save(any(Projeto.class));
    }
}
