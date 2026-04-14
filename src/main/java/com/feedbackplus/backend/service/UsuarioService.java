package com.feedbackplus.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feedbackplus.backend.dtos.UsuarioCadastroDTO;
import com.feedbackplus.backend.model.Pessoa;
import com.feedbackplus.backend.model.Usuario;
import com.feedbackplus.backend.repository.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public String cadastrarNovoUsuario(UsuarioCadastroDTO dados, Integer pessoaTipoId) {
        if (usuarioRepository.existsByLogin(dados.getLogin())) {
            return "Erro: Este usuário ja existe!";
        }

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dados.getNome());
        pessoa.setCpf(dados.getCpf());
        pessoa.setNascimento(dados.getNascimento());
        pessoa.setTelefone(dados.getTelefone());
        pessoa.setAtualizadoEm(LocalDateTime.now());
        pessoa.setPessoaTipoId(pessoaTipoId);

        //Retorna os valores salvos/atualizados direto do banco
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(pessoaSalva.getNome());
        novoUsuario.setLogin(dados.getLogin());
        novoUsuario.setSenha(dados.getSenha());
        novoUsuario.setAtualizadoEm(LocalDateTime.now());

        usuarioRepository.save(novoUsuario);

        return "Usuário cadastrado com sucesso!";

    }

    public boolean realizarLogin(String login, String senha) {
        return usuarioRepository.findByLogin(login)
                .map(user -> user.getSenha().equals(senha))
                .orElse(false);
    }

    public List<Usuario> listar(){
        List<Usuario> lista = usuarioRepository.findAll();
        return lista;
    }

}
