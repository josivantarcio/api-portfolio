package com.jtarcio.portfolioapi.service;

import com.jtarcio.portfolioapi.exception.PortfolioException;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import com.jtarcio.portfolioapi.repository.MembroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do MembroService")
class MembroServiceTest {

    @Mock
    private MembroRepository membroRepository;

    @InjectMocks
    private MembroService membroService;

    private Membro membro;

    @BeforeEach
    void setUp() {
        membro = Membro.builder()
                .id(1L)
                .nome("João Silva")
                .atribuicaoEnum(AtribuicaoEnum.FUNCIONARIO)
                .build();
    }

    @Test
    @DisplayName("Deve buscar membro por ID com sucesso")
    void deveBuscarMembroPorId() {
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));

        Membro resultado = membroService.findById(1L);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals(AtribuicaoEnum.FUNCIONARIO, resultado.getAtribuicaoEnum());
        verify(membroRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar membro inexistente")
    void deveLancarExcecaoAoBuscarMembroInexistente() {
        when(membroRepository.findById(999L)).thenReturn(Optional.empty());

        PortfolioException exception = assertThrows(
                PortfolioException.class,
                () -> membroService.findById(999L)
        );

        assertTrue(exception.getMessage().contains("Não encontramos esse ID"));
    }

    @Test
    @DisplayName("Deve listar todos os membros")
    void deveListarTodosMembros() {
        Membro membro2 = Membro.builder()
                .id(2L)
                .nome("Maria Santos")
                .atribuicaoEnum(AtribuicaoEnum.TERCERIZADO)
                .build();

        when(membroRepository.findAll()).thenReturn(List.of(membro, membro2));

        List<Membro> resultado = membroService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(membroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve salvar membro com sucesso")
    void deveSalvarMembroComSucesso() {
        when(membroRepository.save(any(Membro.class))).thenReturn(membro);

        Membro resultado = membroService.save(membro);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(membroRepository, times(1)).save(any(Membro.class));
    }

    @Test
    @DisplayName("Deve deletar membro com sucesso")
    void deveDeletarMembroComSucesso() {
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));
        doNothing().when(membroRepository).delete(membro);

        membroService.delete(1L);

        verify(membroRepository, times(1)).findById(1L);
        verify(membroRepository, times(1)).delete(membro);
    }

    @Test
    @DisplayName("Deve verificar se membro é FUNCIONARIO")
    void deveVerificarSeMembroEhFuncionario() {
        assertTrue(membro.getAtribuicaoEnum() == AtribuicaoEnum.FUNCIONARIO);
    }

    @Test
    @DisplayName("Deve verificar se membro é TERCERIZADO")
    void deveVerificarSeMembroEhTercerizado() {
        membro.setAtribuicaoEnum(AtribuicaoEnum.TERCERIZADO);

        assertEquals(AtribuicaoEnum.TERCERIZADO, membro.getAtribuicaoEnum());
        assertNotEquals(AtribuicaoEnum.FUNCIONARIO, membro.getAtribuicaoEnum());
    }

    @Test
    @DisplayName("Deve verificar se membro é ACIONISTA")
    void deveVerificarSeMembroEhAcionista() {
        membro.setAtribuicaoEnum(AtribuicaoEnum.ACIONISTA);

        assertEquals(AtribuicaoEnum.ACIONISTA, membro.getAtribuicaoEnum());
    }

    @Test
    @DisplayName("Deve atualizar nome do membro")
    void deveAtualizarNomeMembro() {
        membro.setNome("João Silva Atualizado");
        when(membroRepository.save(any(Membro.class))).thenReturn(membro);

        Membro resultado = membroService.save(membro);

        assertEquals("João Silva Atualizado", resultado.getNome());
    }

    @Test
    @DisplayName("Deve atualizar atribuição do membro")
    void deveAtualizarAtribuicaoMembro() {
        membro.setAtribuicaoEnum(AtribuicaoEnum.ACIONISTA);
        when(membroRepository.save(any(Membro.class))).thenReturn(membro);

        Membro resultado = membroService.save(membro);

        assertEquals(AtribuicaoEnum.ACIONISTA, resultado.getAtribuicaoEnum());
    }

    @Test
    @DisplayName("Deve validar construtor do Membro")
    void deveValidarConstrutorMembro() {
        Membro novoMembro = Membro.builder()
                .nome("Carlos Souza")
                .atribuicaoEnum(AtribuicaoEnum.FUNCIONARIO)
                .build();

        assertNotNull(novoMembro);
        assertNull(novoMembro.getId());
        assertEquals("Carlos Souza", novoMembro.getNome());
        assertEquals(AtribuicaoEnum.FUNCIONARIO, novoMembro.getAtribuicaoEnum());
    }
}
