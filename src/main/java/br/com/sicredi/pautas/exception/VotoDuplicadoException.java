package br.com.sicredi.pautas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class VotoDuplicadoException extends RuntimeException {
    public VotoDuplicadoException(String message) {
        super(message);
    }
}
