package com.feedbackplus.backend.controller;

import com.feedbackplus.backend.model.Usuario;
import com.feedbackplus.backend.service.PessoaService;
import com.feedbackplus.backend.service.UsuarioService;
import com.feedbackplus.backend.dtos.UsuarioCadastroDTO;
import com.feedbackplus.backend.dtos.UsuarioListDTO;
import com.feedbackplus.backend.dtos.UsuarioLoginDTO;
import com.feedbackplus.backend.dtos.UsuarioUpdateDTO;

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
    @Autowired
    private PessoaService pessoaService;

    // Cadastra clientes
    @PostMapping("/cadastrar-cliente")
    public ResponseEntity<?> cadastrarCliente(@RequestBody UsuarioCadastroDTO dados) {

        String resultado = usuarioService.cadastrarNovoUsuario(dados, 3);

        if (!"Usuário cadastrado com sucesso!".equals(resultado)) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    // Cadastra apenas empresas (perfilId = 2)
    @PostMapping("/incluir-usuario")
    public ResponseEntity<String> incluirUsuario(@RequestBody UsuarioCadastroDTO dados,
            @RequestParam Integer perfilId) {
        if (perfilId == null || perfilId != 2) {
            return ResponseEntity.badRequest().body("Somente empresas podem ser cadastradas por este endpoint.");
        }

        String resultado = usuarioService.cadastrarNovoUsuario(dados, perfilId);
        if (!"Usuário cadastrado com sucesso!".equals(resultado)) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDTO dadosLogin) {

        boolean autenticado = usuarioService.autenticarLogin(dadosLogin.getLogin(), dadosLogin.getSenha());

        if (autenticado) {
            var usuario = usuarioService.buscarPorLogin(dadosLogin.getLogin());
            var pessoaTipoId = pessoaService.returnPessoaTipoId(usuario.getUsuarioId());

            // CORREÇÃO: Enviando todos os campos que o login.js espera
            return ResponseEntity.ok(Map.of(
                    "mensagem", "Login realizado com sucesso",
                    "nome", usuario.getNome(),
                    "usuarioId", usuario.getUsuarioId(),
                    "pessoaTipoId", pessoaTipoId));
        } else {
            return ResponseEntity.status(401).body(Map.of("erro", "Login ou senha inválidos"));
        }
    }

    // ---------listar-----------
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioListDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarComTipo());
    }

    // -----Buscar por Id---------
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioUpdateDTO> buscarPorId(@PathVariable Integer id) {
        UsuarioUpdateDTO usuario = usuarioService.buscarDetalhesPorId(id);
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
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody UsuarioUpdateDTO usuarioDados) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuarioDados);

        return ResponseEntity.ok(usuarioAtualizado);
    }

}
