package com.jtarcio.portfolioapi.controller;

import com.jtarcio.portfolioapi.model.entity.Membro;
import com.jtarcio.portfolioapi.service.MembroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    @Operation(summary = "Listar todos os membros", description = "mostra a lista completa de membros")
    public ResponseEntity<List<Membro>> listarTodos() {
        List<Membro> membros = membroService.findAll();
        return ResponseEntity.ok(membros);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar membro por ID", description = "Apresenta membro por busca do id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Membro encontrado"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado")
    })
    public ResponseEntity<Membro> buscarPorId(@PathVariable Long id) {
        Membro membro = membroService.findById(id);
        return ResponseEntity.ok(membro);
    }

    @PostMapping
    @Operation(summary = "Criar novo membro")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Membro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Membro> criar(@RequestBody Membro membro) {
        Membro newMembro = membroService.save(membro);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMembro);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar membro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membro deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado ")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        membroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mock")
    @Operation(summary = "Buscar membros mock")
    public ResponseEntity<List<Membro>> buscarMembrosMock() {
        List<Membro> membros = membroService.buscarMembroMock();
        return ResponseEntity.ok(membros);
    }

}
