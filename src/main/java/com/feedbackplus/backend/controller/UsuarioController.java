package com.feedbackplus.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.feedbackplus.backend.model.Usuario;
import com.feedbackplus.backend.service.UsuarioService;
import com.feedbackplus.backend.dtos.UsuarioCadastroDTO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    // Cadastra clientes
    @PostMapping("/cadastrar-cliente")
    public ResponseEntity<?> cadastrarCliente(@RequestBody UsuarioCadastroDTO dados) {

        String resultado = usuarioService.cadastrarNovoUsuario(dados, 3);

        if (resultado.contains("Conflito")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    // Cadastra usuários-funcionários
    @PostMapping("/cadastrar-funcionario")
    public ResponseEntity<String> cadastrarFuncionario(@RequestBody UsuarioCadastroDTO dados) {
        String resultado = usuarioService.cadastrarNovoUsuario(dados, 2);
        if (resultado.contains("Conflito")) {
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

    @GetMapping("/listar")
    public List<Usuario> listarTodos() {
        List<Usuario> lista = usuarioService.listar();
        return lista;
    }

}
