package com.feedbackplus.backend.dtos;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    private Integer usuarioId;
    private String nome;
    private String login;
    private String senha;
    private String telefone;
    private Integer pessoaTipoId;
}
