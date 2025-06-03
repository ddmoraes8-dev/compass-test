package br.com.sicredi.pautas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessaoFechadaException extends RuntimeException {
    public SessaoFechadaException(String message) {
        super(message);
    }
}
