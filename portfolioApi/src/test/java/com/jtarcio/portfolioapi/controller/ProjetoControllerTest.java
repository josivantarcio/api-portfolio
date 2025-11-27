package com.jtarcio.portfolioapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtarcio.portfolioapi.dto.request.ProjetoRequestDTO;
import com.jtarcio.portfolioapi.dto.response.ProjetoResponseDTO;
import com.jtarcio.portfolioapi.mapper.ProjetoMapper;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.model.entity.Projeto;
import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.ClassificacaoRiscoEnum;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import com.jtarcio.portfolioapi.service.ProjetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin123")
@DisplayName("Testes do ProjetoController")
class ProjetoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjetoService projetoService;

    @MockBean
    private ProjetoMapper projetoMapper;

    private Projeto projeto;
    private ProjetoRequestDTO projetoRequestDTO;
    private ProjetoResponseDTO projetoResponseDTO;
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
                .membros(List.of(membro))
                .build();

        projetoRequestDTO = ProjetoRequestDTO.builder()
                .nome("Projeto Teste")
                .dataInicio(LocalDate.now())
                .previsaoFim(LocalDate.now().plusMonths(2))
                .orcamentoTotal(new BigDecimal("50000"))
                .descricao("Descrição teste")
                .gerenteId(1L)
                .status(StatusProjetoEnum.EM_ANALISE)
                .membrosIds(List.of(1L))
                .build();

        projetoResponseDTO = ProjetoResponseDTO.builder()
                .id(1L)
                .nome("Projeto Teste")
                .dataInicio(LocalDate.now())
                .previsaoFim(LocalDate.now().plusMonths(2))
                .orcamentoTotal(new BigDecimal("50000"))
                .descricao("Descrição teste")
                .classificacaoRiscoEnum(ClassificacaoRiscoEnum.BAIXO_RISCO)
                .status(StatusProjetoEnum.EM_ANALISE)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os projetos com paginação")
    void deveListarTodosProjetos() throws Exception {
        Page<Projeto> page = new PageImpl<>(List.of(projeto));
        when(projetoService.findAll(any(PageRequest.class))).thenReturn(page);
        when(projetoMapper.toResponseDTO(any(Projeto.class))).thenReturn(projetoResponseDTO);

        mockMvc.perform(get("/api/projetos")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Deve buscar projeto por ID")
    void deveBuscarProjetoPorId() throws Exception {
        when(projetoService.findById(1L)).thenReturn(projeto);
        when(projetoMapper.toResponseDTO(any(Projeto.class))).thenReturn(projetoResponseDTO);

        mockMvc.perform(get("/api/projetos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Projeto Teste"));
    }

    @Test
    @DisplayName("Deve criar novo projeto")
    void deveCriarProjeto() throws Exception {
        when(projetoMapper.toEntity(any(ProjetoRequestDTO.class))).thenReturn(projeto);
        when(projetoService.create(any(Projeto.class))).thenReturn(projeto);
        when(projetoMapper.toResponseDTO(any(Projeto.class))).thenReturn(projetoResponseDTO);

        mockMvc.perform(post("/api/projetos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projetoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Projeto Teste"));
    }

    @Test
    @DisplayName("Deve atualizar projeto")
    void deveAtualizarProjeto() throws Exception {
        when(projetoMapper.toEntity(any(ProjetoRequestDTO.class))).thenReturn(projeto);
        when(projetoService.update(eq(1L), any(Projeto.class))).thenReturn(projeto);
        when(projetoMapper.toResponseDTO(any(Projeto.class))).thenReturn(projetoResponseDTO);

        mockMvc.perform(put("/api/projetos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projetoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Projeto Teste"));
    }

    @Test
    @DisplayName("Deve deletar projeto")
    void deveDeletarProjeto() throws Exception {
        mockMvc.perform(delete("/api/projetos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve alterar status do projeto")
    void deveAlterarStatus() throws Exception {
        when(projetoService.alterarStatus(eq(1L), any(StatusProjetoEnum.class))).thenReturn(projeto);
        when(projetoMapper.toResponseDTO(any(Projeto.class))).thenReturn(projetoResponseDTO);

        mockMvc.perform(patch("/api/projetos/1/status")
                        .param("novoStatus", "ANALISE_REALIZADA"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve adicionar membro ao projeto")
    void deveAdicionarMembro() throws Exception {
        when(projetoService.adicionarMembro(1L, 1L)).thenReturn(projeto);
        when(projetoMapper.toResponseDTO(any(Projeto.class))).thenReturn(projetoResponseDTO);

        mockMvc.perform(post("/api/projetos/1/membros/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve remover membro do projeto")
    void deveRemoverMembro() throws Exception {
        when(projetoService.removerMembro(1L, 1L)).thenReturn(projeto);
        when(projetoMapper.toResponseDTO(any(Projeto.class))).thenReturn(projetoResponseDTO);

        mockMvc.perform(delete("/api/projetos/1/membros/1"))
                .andExpect(status().isOk());
    }
}
