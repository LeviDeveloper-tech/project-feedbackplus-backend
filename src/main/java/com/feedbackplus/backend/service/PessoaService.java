package com.feedbackplus.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feedbackplus.backend.model.Pessoa;
import com.feedbackplus.backend.repository.PessoaRepository;

@Service
public class PessoaService{
    @Autowired
    private PessoaRepository pessoaRepository;

    public Integer returnPessoaTipoId(Integer id){
        Optional<Pessoa> pessoa = pessoaRepository.findByPessoaId(id);
        if (pessoa.isPresent()) {
            Integer pessoaTipoId = pessoa.get().getPessoaTipoId();
            // Aqui você pode usar pessoaTipoId ou retornar a Pessoa
            return pessoaTipoId; // ou retorne pessoaTipoId se mudar o tipo de retorno
        } else {
            return null; // ou lance uma exceção, como new RuntimeException("Pessoa não encontrada");
        }
    }  

}