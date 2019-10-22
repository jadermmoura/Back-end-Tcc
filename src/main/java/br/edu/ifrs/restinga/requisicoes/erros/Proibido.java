
package br.edu.ifrs.restinga.requisicoes.erros;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class Proibido extends RuntimeException {
    public Proibido(String erro) {
        super(erro);
    }
    
}
