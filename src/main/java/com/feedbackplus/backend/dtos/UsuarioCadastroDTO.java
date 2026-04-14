package com.feedbackplus.backend.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioCadastroDTO {
    //Pessoa
    private String nome;
    private String cpf;
    private String telefone;
    private LocalDate nascimento;       

    //Usuario
    private String login;
    private String senha;
}
