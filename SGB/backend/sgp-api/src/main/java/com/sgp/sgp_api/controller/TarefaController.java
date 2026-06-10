package com.sgp.sgp_api.controller;

import com.sgp.sgp_api.model.Tarefa;
import com.sgp.sgp_api.model.Usuario;
import com.sgp.sgp_api.repository.TarefaRepository;
import com.sgp.sgp_api.repository.UsuarioRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin(origins = "*")
public class TarefaController {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;

    public TarefaController(
            TarefaRepository tarefaRepository,
            UsuarioRepository usuarioRepository) {

        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Tarefa> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return tarefaRepository.findByUsuarioId(usuarioId);
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody Map<String, String> body) {

        Long usuarioId =
                Long.valueOf(body.get("usuarioId"));

        Usuario usuario =
                usuarioRepository.findById(usuarioId)
                        .orElse(null);

        if (usuario == null) {
            return ResponseEntity.badRequest()
                    .body("Usuário não encontrado");
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(body.get("titulo"));
        tarefa.setDescricao(body.get("descricao"));
        tarefa.setColuna(body.get("coluna"));
        tarefa.setUsuario(usuario);

        return ResponseEntity.ok(
                tarefaRepository.save(tarefa));
    }

    @PutMapping("/{id}/coluna")
    public ResponseEntity<?> atualizarColuna(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        Tarefa tarefa =
                tarefaRepository.findById(id)
                        .orElse(null);

        if (tarefa == null) {
            return ResponseEntity.notFound()
                    .build();
        }

        tarefa.setColuna(body.get("coluna"));

        return ResponseEntity.ok(
                tarefaRepository.save(tarefa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(
            @PathVariable Long id) {

        tarefaRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}