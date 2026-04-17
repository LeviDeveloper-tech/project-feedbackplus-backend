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

        // ------Verificação de Cpf------
        if (pessoaRepository.existsByCpf(dados.getCpf())) {
            return "Conflito no servidor | CPF já cadastrado!";
        }
        // ------Verificação de login------
        if (usuarioRepository.existsByLogin(dados.getLogin())) {
            return "Conflito no servidor | Este usuário ja existe!";
        }

        Integer idFinal;
        if (pessoaTipoId == 1) {
            idFinal = 1;
        } else if (pessoaTipoId == 2) {
            Integer max = usuarioRepository.findMaxIdFuncionario();
            idFinal = (max == null) ? 100 : max + 1;
        } else {
            Integer max = usuarioRepository.findMaxIdCliente();
            idFinal = (max == null) ? 30000 : max + 1;
        }

        Pessoa pessoa = new Pessoa();
        pessoa.setPessoaId(idFinal);
        pessoa.setNome(dados.getNome());
        pessoa.setCpf(dados.getCpf());
        pessoa.setNascimento(dados.getNascimento());
        pessoa.setTelefone(dados.getTelefone());
        pessoa.setAtualizadoEm(LocalDateTime.now());
        pessoa.setPessoaTipoId(pessoaTipoId);

        // Retorna os valores salvos/atualizados direto do banco
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsuarioId(idFinal);
        novoUsuario.setNome(pessoaSalva.getNome());
        novoUsuario.setLogin(dados.getLogin());
        novoUsuario.setSenha(dados.getSenha());
        novoUsuario.setAtualizadoEm(LocalDateTime.now());

        usuarioRepository.save(novoUsuario);

        return "Usuário cadastrado com sucesso!";

    }

    public boolean autenticarLogin(String login, String senha) {
        return usuarioRepository.findByLogin(login)
                .map(user -> user.getSenha().equals(senha))
                .orElse(false);
    }

    public List<Usuario> listar() {
        List<Usuario> lista = usuarioRepository.findAll();
        return lista;
    }

}
