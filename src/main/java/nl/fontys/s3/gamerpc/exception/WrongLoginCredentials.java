package nl.fontys.s3.gamerpc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongLoginCredentials extends ResponseStatusException {
    public WrongLoginCredentials(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
