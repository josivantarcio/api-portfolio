package com.jtarcio.portfolioapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtarcio.portfolioapi.dto.request.MembroRequestDTO;
import com.jtarcio.portfolioapi.dto.response.MembroResponseDTO;
import com.jtarcio.portfolioapi.mapper.MembroMapper;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.model.entity.enums.AtribuicaoEnum;
import com.jtarcio.portfolioapi.service.MembroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin123")
@DisplayName("Testes do MembroController")
class MembroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MembroService membroService;

    @MockBean
    private MembroMapper membroMapper;

    private Membro membro;
    private MembroRequestDTO membroRequestDTO;
    private MembroResponseDTO membroResponseDTO;

    @BeforeEach
    void setUp() {
        membro = Membro.builder()
                .id(1L)
                .nome("João Silva")
                .atribuicaoEnum(AtribuicaoEnum.FUNCIONARIO)
                .build();

        membroRequestDTO = MembroRequestDTO.builder()
                .nome("João Silva")
                .atribuicaoEnum(AtribuicaoEnum.FUNCIONARIO)
                .build();

        membroResponseDTO = MembroResponseDTO.builder()
                .id(1L)
                .nome("João Silva")
                .atribuicaoEnum(AtribuicaoEnum.FUNCIONARIO)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os membros")
    void deveListarTodosMembros() throws Exception {
        when(membroService.findAll()).thenReturn(List.of(membro));
        when(membroMapper.toResponseDTOList(any())).thenReturn(List.of(membroResponseDTO));

        mockMvc.perform(get("/api/membros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve buscar membro por ID")
    void deveBuscarMembroPorId() throws Exception {
        when(membroService.findById(1L)).thenReturn(membro);
        when(membroMapper.toResponseDTO(any(Membro.class))).thenReturn(membroResponseDTO);

        mockMvc.perform(get("/api/membros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve criar novo membro")
    void deveCriarMembro() throws Exception {
        when(membroMapper.toEntity(any(MembroRequestDTO.class))).thenReturn(membro);
        when(membroService.save(any(Membro.class))).thenReturn(membro);
        when(membroMapper.toResponseDTO(any(Membro.class))).thenReturn(membroResponseDTO);

        mockMvc.perform(post("/api/membros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membroRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve atualizar membro")
    void deveAtualizarMembro() throws Exception {
        when(membroService.findById(1L)).thenReturn(membro);
        when(membroService.save(any(Membro.class))).thenReturn(membro);
        when(membroMapper.toResponseDTO(any(Membro.class))).thenReturn(membroResponseDTO);

        mockMvc.perform(put("/api/membros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membroRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve deletar membro")
    void deveDeletarMembro() throws Exception {
        mockMvc.perform(delete("/api/membros/1"))
                .andExpect(status().isNoContent());
    }
}
