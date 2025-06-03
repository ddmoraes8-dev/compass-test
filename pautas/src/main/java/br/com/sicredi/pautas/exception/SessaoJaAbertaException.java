package br.com.sicredi.pautas.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SessaoJaAbertaException extends RuntimeException {
    public SessaoJaAbertaException(String message) {
        super(message);
    }
}
