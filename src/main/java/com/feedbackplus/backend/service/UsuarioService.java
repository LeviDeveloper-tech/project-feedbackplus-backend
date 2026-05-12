package com.feedbackplus.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feedbackplus.backend.dtos.UsuarioCadastroDTO;
import com.feedbackplus.backend.dtos.UsuarioListDTO;
import com.feedbackplus.backend.dtos.UsuarioUpdateDTO;
import com.feedbackplus.backend.model.Pessoa;
import com.feedbackplus.backend.model.Usuario;
import com.feedbackplus.backend.repository.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    // ------CADASTRO DE USUÁRIO-------
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

        LocalDate nascimento = parseDataNascimento(dados.getNascimento());
        if (nascimento == null) {
            return "Formato de data de nascimento inválido. Use yyyy-MM-dd ou dd/MM/yyyy.";
        }
        if (nascimento.getYear() < 1000 || nascimento.isAfter(LocalDate.now())) {
            return "Data de nascimento inválida. Informe uma data real entre 1000 e hoje.";
        }

        Pessoa pessoa = new Pessoa();
        pessoa.setPessoaId(idFinal);
        pessoa.setNome(dados.getNome());
        pessoa.setCpf(dados.getCpf());
        pessoa.setNascimento(nascimento);
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

    private LocalDate parseDataNascimento(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(data);
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                return LocalDate.parse(data, formatter);
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

    // -------Autenticação de Login------------------
    public boolean autenticarLogin(String login, String senha) {
        return usuarioRepository.findByLogin(login)
                .map(user -> user.getSenha().equals(senha))
                .orElse(false);
    }

    // ----------Método Listar---------------
    public List<Usuario> listar() {
        List<Usuario> lista = usuarioRepository.findAll();
        return lista;
    }

    public List<UsuarioListDTO> listarComTipo() {
        return usuarioRepository.findAll().stream().map(usuario -> {
            UsuarioListDTO dto = new UsuarioListDTO();
            dto.setUsuarioId(usuario.getUsuarioId());
            dto.setNome(usuario.getNome());
            dto.setLogin(usuario.getLogin());
            pessoaRepository.findByPessoaId(usuario.getUsuarioId())
                    .ifPresent(pessoa -> dto.setPessoaTipoId(pessoa.getPessoaTipoId()));
            return dto;
        }).toList();
    }

    public void deletar(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário não encontrado para exclusão");
        }

    }

    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login).orElse(null);
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public UsuarioUpdateDTO buscarDetalhesPorId(Integer id) {
        Usuario usuario = buscarPorId(id);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Pessoa pessoa = pessoaRepository.findByPessoaId(id).orElseThrow(
                () -> new RuntimeException("Pessoa não encontrada"));

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setUsuarioId(usuario.getUsuarioId());
        dto.setNome(usuario.getNome());
        dto.setLogin(usuario.getLogin());
        dto.setTelefone(pessoa.getTelefone());
        dto.setPessoaTipoId(pessoa.getPessoaTipoId());
        return dto;
    }

    public Usuario atualizar(Integer id, UsuarioUpdateDTO dadosNovos) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dadosNovos.getNome() != null) {
            usuarioExistente.setNome(dadosNovos.getNome());
        }
        if (dadosNovos.getLogin() != null) {
            usuarioExistente.setLogin(dadosNovos.getLogin());
        }
        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            usuarioExistente.setSenha(dadosNovos.getSenha());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        Pessoa pessoaExistente = pessoaRepository.findByPessoaId(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        pessoaExistente.setNome(usuarioAtualizado.getNome());
        if (dadosNovos.getTelefone() != null) {
            pessoaExistente.setTelefone(dadosNovos.getTelefone());
        }
        if (dadosNovos.getPessoaTipoId() != null) {
            pessoaExistente.setPessoaTipoId(dadosNovos.getPessoaTipoId());
        }

        pessoaRepository.save(pessoaExistente);

        return usuarioAtualizado;
    }
}
