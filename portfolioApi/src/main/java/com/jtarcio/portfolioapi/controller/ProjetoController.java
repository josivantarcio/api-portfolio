package com.jtarcio.portfolioapi.controller;

import com.jtarcio.portfolioapi.dto.request.ProjetoRequestDTO;
import com.jtarcio.portfolioapi.dto.response.ProjetoResponseDTO;
import com.jtarcio.portfolioapi.mapper.ProjetoMapper;
import com.jtarcio.portfolioapi.model.entity.Projeto;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import com.jtarcio.portfolioapi.service.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projetos")
@Tag(name = "Projetos")
@AllArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;
    private final ProjetoMapper projetoMapper;


    /*
    LISTAR TOOS OS PROJETOS PAGINADOS
     */
    @GetMapping
    @Operation(summary = "Listar projetos com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<Page<ProjetoResponseDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Projeto> projetos = projetoService.findAll(pageable);
        Page<ProjetoResponseDTO> response = projetos.map(projetoMapper::toResponseDTO);

        return ResponseEntity.ok(response);
    } //ok


    /*
    LISTA TODOS OS PROJETOS SEM PAGINAÇÃO
     */
    @GetMapping("/all")
    @Operation(summary = "Listar todos os projetos sem paginação")
    public ResponseEntity<List<ProjetoResponseDTO>> listarTodosSemPaginacao() {
        List<Projeto> projetos = projetoService.findAll();
        List<ProjetoResponseDTO> response = projetoMapper.toResponseDTOList(projetos);
        return ResponseEntity.ok(response);
    } //ok


    /*
    BUSCAR PROJETO POR ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar projeto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable Long id) {
        Projeto projeto = projetoService.findById(id);
        ProjetoResponseDTO response = projetoMapper.toResponseDTO(projeto);
        return ResponseEntity.ok(response);
    } //ok


    /*
    CRIAR NOVO PROJETO
     */
    @PostMapping
    @Operation(summary = "Criar novo projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProjetoResponseDTO> criar(@Valid @RequestBody ProjetoRequestDTO projetoRequestDTO) {
        Projeto projeto = projetoMapper.toEntity(projetoRequestDTO);
        Projeto novoProjeto = projetoService.create(projeto);
        ProjetoResponseDTO response = projetoMapper.toResponseDTO(novoProjeto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } //ok


    /*
    ATUALIZAR PROJETO
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProjetoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProjetoRequestDTO projetoRequestDTO) {

        Projeto projetoExistente = projetoMapper.toEntity(projetoRequestDTO);
        Projeto projetoAtualizado = projetoService.update(id, projetoExistente);
        ProjetoResponseDTO response = projetoMapper.toResponseDTO(projetoAtualizado);

        return ResponseEntity.ok(response);
    } //ok

    /*
    DELETAR PROJETO PELO ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projeto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Não é possível deletar projeto neste status")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        projetoService.delete(id);
        return ResponseEntity.noContent().build();
    } //OK


    /*
    ALTERAR STATUS DO PROJETO
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjetoResponseDTO> alterarStatus(@PathVariable Long id, @RequestParam StatusProjetoEnum novoStatus) {

        Projeto projeto = projetoService.alterarStatus(id, novoStatus);
        ProjetoResponseDTO response = projetoMapper.toResponseDTO(projeto);
        return ResponseEntity.ok(response);
    } //ok


    /*
    AVANÇAR PARA O PROXIMO STATUS
     */
    @PatchMapping("/{id}/avancar-status")
    @Operation(summary = "Avançar para próximo status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status avançado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível avançar (status final)"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjetoResponseDTO> avancarStatus(@PathVariable Long id) {
        Projeto projeto = projetoService.avancarProximoStatus(id);
        ProjetoResponseDTO response = projetoMapper.toResponseDTO(projeto);
        return ResponseEntity.ok(response);
    } //ok


    /*
    ADICIONAR MEMBROS NO PROJETO
     */
    @PostMapping("/{projetoId}/membros/{membroId}")
    @Operation(summary = "Adicionar membro ao projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Validação falhou"),
            @ApiResponse(responseCode = "404", description = "Projeto ou membro não encontrado")
    })
    public ResponseEntity<ProjetoResponseDTO> adicionarMembro(@PathVariable Long projetoId, @PathVariable Long membroId) {

        Projeto projeto = projetoService.adicionarMembro(projetoId, membroId);
        ProjetoResponseDTO response = projetoMapper.toResponseDTO(projeto);
        return ResponseEntity.ok(response);
    } //ok

    /*
    REMOVER MEMBROS DO PROJETO
     */
    @DeleteMapping("/{projetoId}/membros/{membroId}")
    @Operation(summary = "Remover membro do projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível remover (mínimo de 1 membro)"),
            @ApiResponse(responseCode = "404", description = "Projeto ou membro não encontrado")
    })
    public ResponseEntity<ProjetoResponseDTO> removerMembro(@PathVariable Long projetoId, @PathVariable Long membroId) {

        Projeto projeto = projetoService.removerMembro(projetoId, membroId);
        ProjetoResponseDTO response = projetoMapper.toResponseDTO(projeto);
        return ResponseEntity.ok(response);
    } //ok


    /*
    RELATORIOS
     */
    @GetMapping("/relatorio")
    @Operation(summary = "Relatório do portfólio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<Map<String, Object>> gerarRelatorio() {
        Map<String, Object> relatorio = projetoService.gerarRelatorioPortfolio();
        return ResponseEntity.ok(relatorio);
    } //ok
}
//ok finalizado e conferido todos