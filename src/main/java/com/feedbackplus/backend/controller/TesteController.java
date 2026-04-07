package com.feedbackplus.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TesteController {
    @GetMapping("/hello")
    public String saudacao() {
        return "Server rodando com sucesso";
    }
}
