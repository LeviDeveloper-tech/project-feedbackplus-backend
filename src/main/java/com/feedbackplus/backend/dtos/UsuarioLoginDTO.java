package com.feedbackplus.backend.dtos;


import lombok.Data;

@Data
public class UsuarioLoginDTO {
    private String login;
    private String senha;
    private Integer pessoaTipoId; // Adicione este campo para armazenar o tipo de pessoa (cliente, funcionário, etc.)

}
