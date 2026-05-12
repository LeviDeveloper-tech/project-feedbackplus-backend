package com.feedbackplus.backend.dtos;

import lombok.Data;

@Data
public class UsuarioListDTO {
    private Integer usuarioId;
    private String nome;
    private String login;
    private Integer pessoaTipoId;
}
