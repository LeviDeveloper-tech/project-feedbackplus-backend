package com.feedbackplus.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.feedbackplus.backend.model.Pessoa;



@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    boolean existsByCpf(String cpf);

    Optional<Pessoa> findByPessoaId(Integer pessoaId);
}
