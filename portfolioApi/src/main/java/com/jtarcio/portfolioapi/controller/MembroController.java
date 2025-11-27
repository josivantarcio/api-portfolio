package com.jtarcio.portfolioapi.controller;

import com.jtarcio.portfolioapi.dto.request.MembroRequestDTO;
import com.jtarcio.portfolioapi.dto.response.MembroResponseDTO;
import com.jtarcio.portfolioapi.mapper.MembroMapper;
import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.service.MembroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membros")
@Tag(name = "Membros")
@AllArgsConstructor
public class MembroController {

    private final MembroService membroService;
    private final MembroMapper membroMapper;

    /*
     * LISTAR TODOS OS MEMBROS
     */
    @GetMapping
    @Operation(summary = "Listar todos os membros", description = "mostra a lista completa de membros")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de membros retornada com sucesso")})
    public ResponseEntity<List<MembroResponseDTO>> listarTodos() {
        List<Membro> membros = membroService.findAll();
        List<MembroResponseDTO> response = membroMapper.toResponseDTOList(membros);
        return ResponseEntity.ok(response);
    } //ok

    /*
     *  LISTAR O MEMBRO POR ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar membro por ID", description = "Apresenta membro por busca do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro encontrado"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado")
    })
    public ResponseEntity<MembroResponseDTO> buscarPorId(@PathVariable Long id) {
        Membro membro = membroService.findById(id);
        MembroResponseDTO response = membroMapper.toResponseDTO(membro);
        return ResponseEntity.ok(response);
    }//ok ok

    /*
     * CRIAR NOVO MEMBRO
     */
    @PostMapping
    @Operation(summary = "Criar novo membro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Membro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MembroResponseDTO> criar(@RequestBody MembroRequestDTO membroDto) {
        Membro newMembro = membroMapper.toEntity(membroDto);
        Membro membroSalvo = membroService.save(newMembro);
        MembroResponseDTO response = membroMapper.toResponseDTO(membroSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }//OK ok

    /*
     * DELETAR MEMBRO
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar membro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membro deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado ")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        membroService.delete(id);
        return ResponseEntity.noContent().build();
    } //ok ok

    /*
    ALTERAR MEMBRO
     */
    @PutMapping("/{id}")
    @Operation(summary = "Alterar membro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro alterado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Membro não encontrado")
    })
    public ResponseEntity<MembroResponseDTO> alterar(@PathVariable Long id, @RequestBody MembroRequestDTO membroDto) {

        Membro membroJaExistente = membroService.findById(id);
        membroJaExistente.setNome(membroDto.getNome());
        membroJaExistente.setAtribuicaoEnum(membroDto.getAtribuicaoEnum());

        Membro membroAtualizado = membroService.save(membroJaExistente);
        MembroResponseDTO response = membroMapper.toResponseDTO(membroAtualizado);

        return ResponseEntity.ok(response);
    } //ok ok

    /*
    LISTA MEMBROS MOCK
     */
    @GetMapping("/mock")
    @Operation(summary = "Buscar membros mock pela API")
    public ResponseEntity<List<Membro>> buscarMembrosMock() {
        List<Membro> membros = membroService.buscarMembroMock();
        return ResponseEntity.ok(membros);
    }

}
