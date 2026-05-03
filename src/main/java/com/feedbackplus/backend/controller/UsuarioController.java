package com.feedbackplus.backend.controller;

import com.feedbackplus.backend.model.Usuario;
import com.feedbackplus.backend.service.UsuarioService;
import com.feedbackplus.backend.dtos.UsuarioCadastroDTO;
import com.feedbackplus.backend.dtos.UsuarioLoginDTO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> cadastrarGerencial(@RequestBody UsuarioCadastroDTO dados,
            @RequestParam Integer perfilId) {
        String resultado = usuarioService.cadastrarNovoUsuario(dados, perfilId);
        if (resultado.contains("Conflito")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDTO dadosLogin) {

        // Verifica se o login existe e se as senhas batem
        boolean autenticado = usuarioService.autenticarLogin(dadosLogin.getLogin(), dadosLogin.getSenha());

        if (autenticado) {
            var usuario = usuarioService.buscarPorLogin(dadosLogin.getLogin());
            // Envia response ok em JSON para o front
            return ResponseEntity.ok(Map.of("mensagem", "Login realizado com sucesso", "nome", usuario.getNome()));
        } else {
            // Envia o response error em JSON para o front
            return ResponseEntity.status(401).body(Map.of("erro", "Login ou senha inválidos"));
        }
    }

    // ---------listar-----------
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // -----Buscar por Id---------
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // ------Deletar------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ------Atualizar------------
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody Usuario usuarioDados) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuarioDados);

        return ResponseEntity.ok(usuarioAtualizado);
    }

}
