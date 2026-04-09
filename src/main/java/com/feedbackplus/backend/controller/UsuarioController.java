package com.feedbackplus.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.feedbackplus.backend.model.Pessoa;
import com.feedbackplus.backend.service.UsuarioService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(
            @RequestBody Pessoa pessoa,
            @RequestParam String login,
            @RequestParam String senha) {

        String resultado = usuarioService.cadastrarNovoUsuario(pessoa, login, senha);

        if (resultado.contains("Erro")) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dadosLogin) {
        String login = dadosLogin.get("Login");
        String senha = dadosLogin.get("senha");

        boolean autenticado = usuarioService.realizarLogin(login, senha);

        if (autenticado) {
            return ResponseEntity.ok("{\"mensagem\": \"Login realizado com sucesso!\"}");
        } else {
            return ResponseEntity.status(401).body("{\"erro\": \"Login ou senha inválidos\"}");
        }
    }
}
