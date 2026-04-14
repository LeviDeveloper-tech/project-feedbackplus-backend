package com.feedbackplus.backend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbPessoas")
@Data
public class Pessoa {
    @Id
    @Column(name = "pessoa_id")
    private Integer pessoaId;

    private String nome;
    private String cpf;

    @Column(name = "nascimento")
    private LocalDate nascimento;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "pessoa_tipo_id")
    private Integer pessoaTipoId;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "atualizado_por")
    private Integer atualizadoPor;

}
