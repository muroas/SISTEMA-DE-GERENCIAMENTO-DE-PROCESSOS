package com.sgp.sgp_api.controller;

import com.sgp.sgp_api.model.Usuario;
import com.sgp.sgp_api.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("E-mail já cadastrado");
        }

        Usuario salvo = usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of(
                "id", salvo.getId(),
                "nome", salvo.getNome(),
                "email", salvo.getEmail()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {

        Usuario encontrado = usuarioRepository
                .findByEmail(usuario.getEmail())
                .orElse(null);

        if (encontrado == null ||
                !encontrado.getSenha().equals(usuario.getSenha())) {

            return ResponseEntity.status(401)
                    .body("E-mail ou senha incorretos");
        }

        return ResponseEntity.ok(Map.of(
                "id", encontrado.getId(),
                "nome", encontrado.getNome(),
                "email", encontrado.getEmail()
        ));
    }
}