package br.edu.ifrs.restinga.requisicoes.erros;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErroServidor extends RuntimeException {
    public ErroServidor(String erro) {
        super(erro);
    }
}

