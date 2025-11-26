package com.jtarcio.portfolioapi.controller;

import com.jtarcio.portfolioapi.model.entity.Projeto;
import com.jtarcio.portfolioapi.model.entity.enums.StatusProjetoEnum;
import com.jtarcio.portfolioapi.service.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping
    @Operation(summary = "Listar projetos com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<Page<Projeto>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Projeto> projetos = projetoService.findAll(pageable);
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos os projetos sem paginação")
    public ResponseEntity<List<Projeto>> listarTodosSemPaginacao() {
        List<Projeto> projetos = projetoService.findAll();
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar projeto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<Projeto> buscarPorId(@PathVariable Long id) {
        Projeto projeto = projetoService.findById(id);
        return ResponseEntity.ok(projeto);
    }

    @PostMapping
    @Operation(summary = "Criar novo projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Projeto> criar(@RequestBody Projeto projeto) {
        Projeto novoProjeto = projetoService.create(projeto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProjeto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Projeto> atualizar(
            @PathVariable Long id,
            @RequestBody Projeto projeto) {
        Projeto projetoAtualizado = projetoService.update(id, projeto);
        return ResponseEntity.ok(projetoAtualizado);
    }

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
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<Projeto> alterarStatus(
            @PathVariable Long id,
            @RequestParam StatusProjetoEnum novoStatus) {
        Projeto projeto = projetoService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok(projeto);
    }

    @PatchMapping("/{id}/avancar-status")
    @Operation(summary = "Avançar para próximo status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status avançado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível avançar (status final)"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<Projeto> avancarStatus(@PathVariable Long id) {
        Projeto projeto = projetoService.avancarProximoStatus(id);
        return ResponseEntity.ok(projeto);
    }

    @PostMapping("/{projetoId}/membros/{membroId}")
    @Operation(summary = "Adicionar membro ao projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Validação falhou"),
            @ApiResponse(responseCode = "404", description = "Projeto ou membro não encontrado")
    })
    public ResponseEntity<Projeto> adicionarMembro(
            @PathVariable Long projetoId,
            @PathVariable Long membroId) {
        Projeto projeto = projetoService.adicionarMembro(projetoId, membroId);
        return ResponseEntity.ok(projeto);
    }

    @DeleteMapping("/{projetoId}/membros/{membroId}")
    @Operation(summary = "Remover membro do projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível remover (mínimo de 1 membro)"),
            @ApiResponse(responseCode = "404", description = "Projeto ou membro não encontrado")
    })
    public ResponseEntity<Projeto> removerMembro(
            @PathVariable Long projetoId,
            @PathVariable Long membroId) {
        Projeto projeto = projetoService.removerMembro(projetoId, membroId);
        return ResponseEntity.ok(projeto);
    }


    @GetMapping("/relatorio")
    @Operation(summary = "Relatório do portfólio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<Map<String, Object>> gerarRelatorio() {
        Map<String, Object> relatorio = projetoService.gerarRelatorioPortfolio();
        return ResponseEntity.ok(relatorio);
    }
}
