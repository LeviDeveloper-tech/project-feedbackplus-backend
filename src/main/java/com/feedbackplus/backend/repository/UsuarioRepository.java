package com.feedbackplus.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.feedbackplus.backend.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT MAX(u.usuarioId) FROM Usuario u WHERE u.usuarioId BETWEEN 100 AND 29999")
    Integer findMaxIdFuncionario();

    @Query("SELECT MAX(u.usuarioId) FROM Usuario u WHERE u.usuarioId >= 30000")
    Integer findMaxIdCliente();

    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findById(Integer id);

    boolean existsByLogin(String login);
    boolean existsById(Integer id);
}
